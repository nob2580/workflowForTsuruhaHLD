package eteam.gyoumu.system;

import java.util.List;

import eteam.base.EteamAbstractAction;
import eteam.base.EteamConnection;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting.DENPYOU_KBN;
import eteam.common.HfUfSeigyo;
import eteam.common.KaishaInfo;
import eteam.common.KaishaInfo.ColumnName;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.database.abstractdao.ShiwakePatternMasterAbstractDao;
import eteam.database.dao.ShiwakePatternMasterDao;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 画面項目管理Action
 */
@Getter @Setter @ToString
public class GamenKoumokuKanriAction extends EteamAbstractAction {

	//＜定数＞

	//＜画面入力＞
	/** 画面項目名 */
	String[] koumokuName;
	/** 表示フラグ */
	String[] hyoujiFlg;
	/** 必須フラグ */
	String[] hissuFlg;
	/** 帳票表示フラグ */
	String[] pdfHyoujiFlg;
	/** コード印字フラグ */
	String[] codeOutputFlg;
	/** タブID */
	String tabId = "setting_A001";
	
	//＜画面入力以外＞
	/** 伝票区分 */
	String[] denpyouKbn;
	/** 項目ID */
	String[] koumokuId;
	/** 画面項目一覧 */
	List<GMap> gamenKoumouList;
	/** HF・UF制御クラス */
	HfUfSeigyo hfUfSeigyo = new HfUfSeigyo();
	/** タブに表示するリスト */
	List<GMap> gamenHyoujiList;
	
	//＜入力チェック＞
	@Override
	protected void formatCheck() {
	}
	@Override
	protected void hissuCheck(int eventNum) {}

	//＜イベント＞
	/**
	 * 初期表示イベント
	 * @return 処理結果
	 */
	public String init() {
		try(EteamConnection connection = EteamConnection.connect()) {
			
			
			//1.入力チェック なし
			//2.データ存在チェック なし
			//3.アクセス可能チェック なし

			//4.処理（初期表示）
			displaySeigyo(connection);
			
			// 更新用に配列にする
			denpyouKbn = new String[gamenKoumouList.size()];
			koumokuId = new String[gamenKoumouList.size()];
			koumokuName = new String[gamenKoumouList.size()];
			hyoujiFlg = new String[gamenKoumouList.size()];
			hissuFlg = new String[gamenKoumouList.size()];
			pdfHyoujiFlg = new String[gamenKoumouList.size()];
			codeOutputFlg = new String[gamenKoumouList.size()];
			int i = 0;
			for (GMap record : gamenKoumouList) {
				denpyouKbn[i] = (String)record.get("denpyou_kbn");
				
				String id = (String)record.get("koumoku_id");
				String name = (String)record.get("koumoku_name");

				//HF UFは会社情報の文言を出す(画面項目制御上の項目名は特にいみない)
				//UF固定値はもともと非表示なので考慮しない
				if (id.indexOf("hf1_cd") >= 0) {
					name = KaishaInfo.getKaishaInfo(ColumnName.HF1_NAME);
				}
				if (id.indexOf("hf2_cd") >= 0) {
					name = KaishaInfo.getKaishaInfo(ColumnName.HF2_NAME);
				}
				if (id.indexOf("hf3_cd") >= 0) {
					name = KaishaInfo.getKaishaInfo(ColumnName.HF3_NAME);
				}
				if (id.indexOf("hf4_cd") >= 0) {
					name = KaishaInfo.getKaishaInfo(ColumnName.HF4_NAME);
				}
				if (id.indexOf("hf5_cd") >= 0) {
					name = KaishaInfo.getKaishaInfo(ColumnName.HF5_NAME);
				}
				if (id.indexOf("hf6_cd") >= 0) {
					name = KaishaInfo.getKaishaInfo(ColumnName.HF6_NAME);
				}
				if (id.indexOf("hf7_cd") >= 0) {
					name = KaishaInfo.getKaishaInfo(ColumnName.HF7_NAME);
				}
				if (id.indexOf("hf8_cd") >= 0) {
					name = KaishaInfo.getKaishaInfo(ColumnName.HF8_NAME);
				}
				if (id.indexOf("hf9_cd") >= 0) {
					name = KaishaInfo.getKaishaInfo(ColumnName.HF9_NAME);
				}
				if (id.indexOf("hf10_cd") >= 0) {
					name = KaishaInfo.getKaishaInfo(ColumnName.HF10_NAME);
				}
				if (id.indexOf("uf1_cd") >= 0) {
					name = KaishaInfo.getKaishaInfo(ColumnName.UF1_NAME);
				}
				if (id.indexOf("uf2_cd") >= 0) {
					name = KaishaInfo.getKaishaInfo(ColumnName.UF2_NAME);
				}
				if (id.indexOf("uf3_cd") >= 0) {
					name = KaishaInfo.getKaishaInfo(ColumnName.UF3_NAME);
				}
				if (id.indexOf("uf4_cd") >= 0) {
					name = KaishaInfo.getKaishaInfo(ColumnName.UF4_NAME);
				}
				if (id.indexOf("uf5_cd") >= 0) {
					name = KaishaInfo.getKaishaInfo(ColumnName.UF5_NAME);
				}
				if (id.indexOf("uf6_cd") >= 0) {
					name = KaishaInfo.getKaishaInfo(ColumnName.UF6_NAME);
				}
				if (id.indexOf("uf7_cd") >= 0) {
					name = KaishaInfo.getKaishaInfo(ColumnName.UF7_NAME);
				}
				if (id.indexOf("uf8_cd") >= 0) {
					name = KaishaInfo.getKaishaInfo(ColumnName.UF8_NAME);
				}
				if (id.indexOf("uf9_cd") >= 0) {
					name = KaishaInfo.getKaishaInfo(ColumnName.UF9_NAME);
				}
				if (id.indexOf("uf10_cd") >= 0) {
					name = KaishaInfo.getKaishaInfo(ColumnName.UF10_NAME);
				}
				//差引回数は会社設定の文言を出す(画面項目制御上の項目名は特にいみない)
				if ((denpyouKbn[i].equals(DENPYOU_KBN.RYOHI_SEISAN) || denpyouKbn[i].equals(DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI)) && id.equals("sashihiki_num")){
					name = setting.sashihikiName();
				}
				if ((denpyouKbn[i].equals(DENPYOU_KBN.KAIGAI_RYOHI_SEISAN) || denpyouKbn[i].equals(DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI)) && id.equals("sashihiki_num")){
					name = setting.sashihikiNameKaigai();
				}
				
				koumokuId[i] = id;
				koumokuName[i] = name;
				hyoujiFlg[i] = (String)record.get("hyouji_flg");
				hissuFlg[i] = (String)record.get("hissu_flg");
				pdfHyoujiFlg[i] = (String)record.get("pdf_hyouji_flg");
				codeOutputFlg[i] = (String)record.get("code_output_flg");
				i++;
			}

			//5.戻り値を返す
			return "success";
			
		}
	}
		
	/**
	 * 更新イベント
	 * 更新ボタン押下時
	 * システム管理画面で設定した内容を更新する。
	 * @return 処理結果
	 */
	public String koushin() {
		try(EteamConnection connection = EteamConnection.connect()) {
			GamenKoumokuKanriLogic myLogic = EteamContainer.getComponent(GamenKoumokuKanriLogic.class, connection);
			ShiwakePatternMasterAbstractDao shiwakePatternMasterDao = EteamContainer.getComponent(ShiwakePatternMasterDao.class, connection);

			//1.入力チェック
			formatCheck();
			hissuCheck(2);
			
			//2.データ存在チェック なし
			//3.アクセス可能チェック なし

			if(0 < errorList.size()) {
				displaySeigyo(connection);
				return "error";
			}
			
			//4.処理
			
			for (int i = 0; i < denpyouKbn.length; i++) {
				// 画面項目制御レコード単位更新
				myLogic.updateGamenKoumokuSeigyo(denpyouKbn[i], koumokuId[i], koumokuName[i], hyoujiFlg[i], hissuFlg[i], pdfHyoujiFlg[i], codeOutputFlg[i]);
				
				//UFを非表示にしたら取引の該当UFをクリア
				for (int j = 1; j <= 10; j++) {
					if(koumokuId[i].equals("uf" + j + "_cd") && hyoujiFlg[i].equals("0")) shiwakePatternMasterDao.clearUf(j, denpyouKbn[i], getUser().getTourokuOrKoushinUserId());
				}
			}
			
			connection.commit();
			
			//5.戻り値を返す
			return "success";
		}
	}

	/**
	 * データ取得
	 * @param connection コネクション
	 */
	protected void displaySeigyo(EteamConnection connection) {
		SystemKanriCategoryLogic systemLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, connection);

		//設定情報取得
		gamenKoumouList = systemLogic.loadGamenKoumokuWithDenpyouSyubetsu();
		
		//表示する伝票区分・伝票種別の取得
		gamenHyoujiList = systemLogic.loadGamenKoumokuWithDenpyouIchiran();
	}
}
