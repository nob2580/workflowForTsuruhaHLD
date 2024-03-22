package eteam.gyoumu.kaikei;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamCommon;
import eteam.common.EteamConst.Sessionkey;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.HfUfSeigyo;
import eteam.common.open21.Open21Env;
import eteam.common.open21.Open21Env.Version;
import eteam.common.select.KihyouNaviCategoryLogic;
import eteam.gyoumu.kaikei.SeikyuushoBaraiCSVUploadSessionInfo.UploadStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 請求書払い（CSVアップロード確認）画面Action
 */
@Getter @Setter @ToString
public class SeikyuushoBaraiCSVUploadKakuninAction extends EteamAbstractAction {

//＜定数＞

//＜画面入力＞

//＜セッション＞
	/** CSVファイル名 */
	String csvFileName;
	/** CSVファイル情報（申請内容）リスト */
	List<SeikyuushoBaraiCSVUploadDenpyouInfo> denpyouList;

//＜画面制御情報＞
	/** HF・UF制御クラス */
	HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();
	/** ステータス */
	UploadStatus status;
	/** SIASバージョンか判別 **/
	boolean issias = (Open21Env.getVersion() == Version.SIAS ) ? true : false; // (変数名が小文字でないとjspで読み取れない？)
	/** 予算執行対象が支出依頼か判別 **/
	boolean isshishutsuirai; // (変数名が小文字でないとjspで読み取れない？)
	/** 請求書払い入力方式 */
	String nyuryokuHoushiki = setting.zeiDefaultA003();
	//入力方式とか事業者区分の変更可否とか、設定値画面に出さなくていい？

//＜部品＞
	/** 起票ナビカテゴリーロジック*/
	protected KihyouNaviCategoryLogic kihyouLogic;
	
//＜入力チェック＞
	@Override
	protected void formatCheck() {}

	@Override
	protected void hissuCheck(int eventNum) {}

//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return ResultName
	 */
	public String init(){
		try(EteamConnection connection = EteamConnection.connect()) {
			setConnection(connection);
			
			if (status == null)
			{
				// 初回
				status = UploadStatus.Init;
			}
			
			//セション情報を取得する。
			SeikyuushoBaraiCSVUploadSessionInfo sessionInfo = (SeikyuushoBaraiCSVUploadSessionInfo)session.get(Sessionkey.SEIKYUUSHO_CSV);
			if (null == sessionInfo) {
				errorList.add("CSVファイルのアップロードからやり直してください。");
				return "error";
			}
			csvFileName = sessionInfo.getFileName();
			denpyouList = sessionInfo.getDenpyouList();
			errorList = sessionInfo.getErrorList();
			
			//予算執行対象が支出依頼か判別
			judgeShishutsuIrai();
			
			if (sessionInfo.getStatus() != null)
			{
				// statusがgetできたら非同期処理終了と判定
				status = sessionInfo.getStatus();
				session.remove(Sessionkey.SEIKYUUSHO_CSV);
			}

		// 戻り値を返す
		return "success";
		
		}
	}

	/**
	 * 登録イベント
	 * @return ResultName
	 */
	public String touroku(){
		try(EteamConnection connection = EteamConnection.connect()) {
			setConnection(connection);
			
			//セション情報を取得する。
			SeikyuushoBaraiCSVUploadSessionInfo sessionInfo = (SeikyuushoBaraiCSVUploadSessionInfo)session.get(Sessionkey.SEIKYUUSHO_CSV);
			if (null == sessionInfo) {
				errorList.add("CSVファイルのアップロードからやり直してください。");
				return "error";
			}
			csvFileName = sessionInfo.getFileName();
			denpyouList = sessionInfo.getDenpyouList();
			errorList = sessionInfo.getErrorList();
			errorList.add("登録処理中です。");
			
			//予算執行対象が支出依頼か判別
			judgeShishutsuIrai();
			
			// 処理
			SeikyuushoBaraiCSVUploadThread myThread = new SeikyuushoBaraiCSVUploadThread(session, sessionInfo, EteamCommon.getContextSchemaName());
			myThread.start();
			
			status = UploadStatus.Run;
			return "success";

		}
	}
	
	/**
	 * 予算執行対象が支出依頼か判別するメソッド
	 */
	protected void judgeShishutsuIrai() {
		GMap denpyouShubetsuMap = kihyouLogic.findDenpyouKanri(DENPYOU_KBN.SEIKYUUSHO_BARAI);
		String yosanShikkouTaishou = denpyouShubetsuMap.get("yosan_shikkou_taishou").toString();
		isshishutsuirai = (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI.equals(yosanShikkouTaishou)) ? true : false;
	}
	
	/**
	 * コネクション設定を行います。
	 * @param connection コネクション
	 */
	protected void setConnection(EteamConnection connection) {
		kihyouLogic = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, connection);
	}

}
