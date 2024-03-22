package eteam.gyoumu.kihyounavi;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.select.KihyouNaviCategoryLogic;
import eteam.common.select.SystemKanriCategoryLogic;
import eteam.gyoumu.kanitodoke.KaniTodokeLogic;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 伝票管理Logic
 */
@Getter @Setter @ToString
public class DenpyouKanriLogic extends EteamAbstractLogic {

//＜定数＞
	/** 伝票種別一覧：カラム名：起案番号運用フラグ */
	public static final String KIAN_BANGOU_UNYOU_FLG = "kianbangou_unyou_flg";
	/** 伝票種別一覧：カラム名：予算執行対象 */
	public static final String YOSAN_SHIKKOU_TAISHOU = "yosan_shikkou_taishou";
	/** 伝票種別一覧：カラム名：ルート判定金額 */
	public static final String ROUTE_HANTEI_KINGAKU = "route_hantei_kingaku";
	/** 伝票種別一覧：カラム名：承認状況欄印刷フラグ */
	public static final String SHOUNIN_JYOUKYOU_PRINT_FLG = "shounin_jyoukyou_print_flg";

	/** エラーメッセージ：予算執行対象組み合わせチェック */
	protected static final String[] YOSAN_SHIKKOU_TAISHOU_PATTERN_ERR = {"EDPKR001", "予算執行対象に[%s]は指定できません。"};
	/** エラーメッセージ：ルート判定金額項目存在チェック */
	protected static final String[] ROUTE_HANTEI_KINGAKU_NOT_EXIST_ERR = {"EDPKR002", "ルート判定金額に指定した項目はユーザー定義届書レイアウトに存在しません。"};
	/** エラーメッセージ：予算執行対象項目存在チェック */
	protected static final String[] YOSAN_SHIKKOU_TAISHOU_NOT_EXIST_ERR = {"EDPKR003", "予算執行管理に必要な[%s]の入力部品がユーザー定義届書レイアウトに存在しません。"};

	/**
	 * 伝票種別一覧を更新する。
	 * @param hyouji_jun 表示順
	 * @param gyoumu_shubetsu 業務種別
	 * @param denpyou_shubetsu 伝票種別
	 * @param denpyou_karibarai_nashi_shubetsu 伝票種別（仮払なし）
	 * @param denpyou_print_shubetsu 伝票種別（帳票）
	 * @param denpyou_print_karibarai_nashi_shubetsu 伝票種別（帳票・仮払なし）
	 * @param naiyou 内容
	 * @param yuukou_kigen_from 有効期限開始日
	 * @param yuukou_kigen_to 有効期限終了日
	 * @param kanren_sentaku_flg 関連伝票選択フラグ
	 * @param kanren_hyouji_flg 関連伝票入力欄表示フラグ
	 * @param denpyou_print_flg 申請時伝票出力フラグ
	 * @param kianbangou_unyou_flg 起案番号運用フラグ
	 * @param yosan_shikkou_taishou 予算執行対象
	 * @param route_hantei_kingaku ルート判定金額
	 * @param route_torihiki_flg ルート取引毎設定フラグ
	 * @param shounin_jyoukyou_print_flg 承認状況欄印刷フラグ
	 * @param shinsei_shori_kengen_name 申請処理権限名
	 * @param shiiresakiFlg 仕入先フラグ
	 * @param user_id ユーザID
	 * @param denpyou_kbn 伝票区分
	 * @return 更新件数
	 */
	public int update(int hyouji_jun
					,String gyoumu_shubetsu
					,String denpyou_shubetsu
					,String denpyou_karibarai_nashi_shubetsu
					,String denpyou_print_shubetsu
					,String denpyou_print_karibarai_nashi_shubetsu
					,String naiyou
					,Date yuukou_kigen_from
					,Date yuukou_kigen_to
					,String kanren_sentaku_flg
					,String kanren_hyouji_flg
					,String denpyou_print_flg
					,String kianbangou_unyou_flg
					,String yosan_shikkou_taishou
					,String route_hantei_kingaku
					,String route_torihiki_flg
					,String shounin_jyoukyou_print_flg
					,String shinsei_shori_kengen_name
					,String shiiresakiFlg
					,String user_id
					,String denpyou_kbn) {
		final String sql = "UPDATE denpyou_shubetsu_ichiran SET" +
								" hyouji_jun=?" +
								",gyoumu_shubetsu=?" +
								",denpyou_shubetsu=?" +
								",denpyou_karibarai_nashi_shubetsu=?" +
								",denpyou_print_shubetsu=?" +
								",denpyou_print_karibarai_nashi_shubetsu=?" +
								",naiyou=?" +
								",yuukou_kigen_from=?" +
								",yuukou_kigen_to=?" +
								",kanren_sentaku_flg=?" +
								",kanren_hyouji_flg=?" +
								",denpyou_print_flg=?" +
								",kianbangou_unyou_flg=?" +
								",yosan_shikkou_taishou=?" +
								",route_hantei_kingaku=?" +
								",route_torihiki_flg=?" +
								",shounin_jyoukyou_print_flg=?" +
								",shinsei_shori_kengen_name=?" +
								",shiiresaki_flg=?" +
								",koushin_user_id=?,koushin_time=current_timestamp" +
							" WHERE denpyou_kbn=?";
		
		return connection.update(sql
								,hyouji_jun
								,gyoumu_shubetsu
								,denpyou_shubetsu
								,denpyou_karibarai_nashi_shubetsu
								,denpyou_print_shubetsu
								,denpyou_print_karibarai_nashi_shubetsu
								,naiyou
								,yuukou_kigen_from
								,yuukou_kigen_to
								,kanren_sentaku_flg
								,kanren_hyouji_flg
								,denpyou_print_flg
								,kianbangou_unyou_flg
								,yosan_shikkou_taishou
								,route_hantei_kingaku
								,route_torihiki_flg
								,shounin_jyoukyou_print_flg
								,shinsei_shori_kengen_name
								,shiiresakiFlg
								,user_id, denpyou_kbn);

	}

	/**
	 * 起案番号運用活性判定<br>
	 * 伝票区分により「起案番号運用する／しない」項目の活性制御判定を行って結果を返却する。<br>
	 * 
	 * @param denpyouKbn 伝票区分
	 * @return 0:非活性 1:活性
	 */
	public String isActiveKianBangouUnyou(String denpyouKbn){
		String result = "0";

		// ワークフローイベント制御Logic
		WorkflowEventControlLogic wfEventLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, super.connection);

		/*
		 * 伝票区分により活性制御判定を行う。
		 */
		if (wfEventLogic.isKaniTodoke(denpyouKbn)){
			// ユーザー定義届書伝票の場合は活性
			result = "1";

		}else{
			switch (denpyouKbn){
			case EteamNaibuCodeSetting.DENPYOU_KBN.KEIHI_TATEKAE_SEISAN:
				// 経費立替精算
			case EteamNaibuCodeSetting.DENPYOU_KBN.KARIBARAI_SHINSEI:
				// 仮払申請
			case EteamNaibuCodeSetting.DENPYOU_KBN.SEIKYUUSHO_BARAI:
				// 請求書払申請
			case EteamNaibuCodeSetting.DENPYOU_KBN.SIHARAIIRAI:
				// 支払依頼
			case EteamNaibuCodeSetting.DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI:
				// 出張伺い申請（仮払申請）
			case EteamNaibuCodeSetting.DENPYOU_KBN.RYOHI_SEISAN:
				// 出張旅費精算（仮払精算）
			case EteamNaibuCodeSetting.DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI:
				// 海外出張伺い申請（仮払申請）
			case EteamNaibuCodeSetting.DENPYOU_KBN.KAIGAI_RYOHI_SEISAN:
				// 海外出張旅費精算（仮払精算）

				// 活性
				result = "1";
				break;
			default:
				// その他の伝票は非活性
				break;
			}
		}
		return result;
	}

	/**
	 * 予算執行対象活性判定<br>
	 * 伝票区分により「予算執行対象」項目の活性制御判定を行って結果を返却する。<br>
	 * 
	 * @param denpyouKbn 伝票区分
	 * @return 0:非活性 1:活性
	 */
	public String isActiveYosanShikkouTaishou(String denpyouKbn){
		String result = "0";

		// ワークフローイベント制御Logic
		WorkflowEventControlLogic wfEventLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, super.connection);

		/*
		 * 伝票区分により活性制御判定を行う。
		 */
		if (wfEventLogic.isKaniTodoke(denpyouKbn)){
			// ユーザー定義届書伝票の場合は活性
			result = "1";

		}else{
			switch (denpyouKbn){
			case EteamNaibuCodeSetting.DENPYOU_KBN.KEIHI_TATEKAE_SEISAN:
				// 経費立替精算
			case EteamNaibuCodeSetting.DENPYOU_KBN.KARIBARAI_SHINSEI:
				// 仮払申請
			case EteamNaibuCodeSetting.DENPYOU_KBN.SEIKYUUSHO_BARAI:
				// 請求書払申請
			case EteamNaibuCodeSetting.DENPYOU_KBN.SIHARAIIRAI:
				// 支払依頼
			case EteamNaibuCodeSetting.DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI:
				// 出張伺い申請（仮払申請）
			case EteamNaibuCodeSetting.DENPYOU_KBN.RYOHI_SEISAN:
				// 出張旅費精算（仮払精算）
			case EteamNaibuCodeSetting.DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI:
				// 海外出張伺い申請（仮払申請）
			case EteamNaibuCodeSetting.DENPYOU_KBN.KAIGAI_RYOHI_SEISAN:
				// 海外出張旅費精算（仮払精算）

				// 活性
				result = "1";
				break;
			default:
				// その他の伝票は非活性
				break;
			}
		}
		return result;
	}

	/**
	 * ルート判定金額活性判定<br>
	 * 伝票区分により「ルート判定金額」項目の活性制御判定を行って結果を返却する。<br>
	 * 
	 * @param denpyouKbn 伝票区分
	 * @return 0:非活性 1:活性
	 */
	public String isActiveRoueHanteiKingaku(String denpyouKbn){
		String result = "0";

		// ワークフローイベント制御Logic
		WorkflowEventControlLogic wfEventLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, super.connection);

		/*
		 * 伝票区分により活性制御判定を行う。
		 */
		if (wfEventLogic.isKaniTodoke(denpyouKbn)){
			// ユーザー定義届書伝票の場合は活性
			result = "1";
		}
		return result;
	}

	/**
	 * 予算執行対象組み合わせチェック<br>
	 * 伝票に指定した「予算執行対象」値と伝票区分の組み合わせについてチェックする。<br>
	 * 
	 * @param denpyouKbn 伝票区分
	 * @param yosanShikkouTaishou 画面で指定された予算執行対象
	 * @param kianBangouUnyouFlg 起案番号運用フラグ
	 * @return エラーリスト
	 */
	public List<Map<String, String>> chkPatternYosanShikkouTaishou(String denpyouKbn, String yosanShikkouTaishou, String kianBangouUnyouFlg){
		List<Map<String, String>> lstError = new ArrayList<Map<String, String>>();

		// ワークフローイベント制御Logic
		WorkflowEventControlLogic wfEventLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, super.connection);
		// システムカテゴリー内のSelect文を集約したLogic
		SystemKanriCategoryLogic systemLogic = EteamContainer.getComponent(SystemKanriCategoryLogic.class, super.connection);

		/*
		 * 伝票区分により組み合わせをチェックする。
		 */
		boolean isError = false;
		if (wfEventLogic.isKaniTodoke(denpyouKbn)){
			// ユーザー定義届書伝票の場合、画面指定値が「支出依頼」の場合はエラー
			if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI.equals(yosanShikkouTaishou)){
				isError = true;
			 }

		}else{

			switch (denpyouKbn){
			case EteamNaibuCodeSetting.DENPYOU_KBN.KEIHI_TATEKAE_SEISAN:
				// 経費立替精算
			case EteamNaibuCodeSetting.DENPYOU_KBN.SEIKYUUSHO_BARAI:
				// 請求書払申請
			case EteamNaibuCodeSetting.DENPYOU_KBN.SIHARAIIRAI:
				// 支払依頼
			case EteamNaibuCodeSetting.DENPYOU_KBN.RYOHI_SEISAN:
				// 出張旅費精算（仮払精算）
			case EteamNaibuCodeSetting.DENPYOU_KBN.KAIGAI_RYOHI_SEISAN:
				// 海外出張旅費精算（仮払精算）

				// 画面指定値が「実施起案」「支出起案」の場合はエラー
				if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.JISSHI_KIAN.equals(yosanShikkouTaishou)
				 || EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_KIAN.equals(yosanShikkouTaishou)){
					isError = true;
				 }
				break;

			case EteamNaibuCodeSetting.DENPYOU_KBN.KARIBARAI_SHINSEI:
				// 仮払申請
			case EteamNaibuCodeSetting.DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI:
				// 出張伺い申請（仮払申請）
			case EteamNaibuCodeSetting.DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI:
				// 海外出張伺い申請（仮払申請）

				// 画面指定値が「支出起案」「支出依頼」の場合はエラー
				if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_KIAN.equals(yosanShikkouTaishou)
				 || EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI.equals(yosanShikkouTaishou)){
					isError = true;
				 }
				break;

			default:
				// その他の伝票はチェック不要
				break;
			}
		}
		
		// カスタマイズ向け拡張チェック
		isError = isErrorPatternYosanShikkouTaishouExt(denpyouKbn, yosanShikkouTaishou, isError);

		// エラーの場合はメッセージを設定する。
		if (isError){
			// 画面で指定した「予算執行対象」の名称を取得する。
			String yosanShikkouTaishouNm = systemLogic.findNaibuCdSetting("yosan_shikkou_taishou", yosanShikkouTaishou).get("name").toString();

			Map<String, String> mapError = new HashMap<String, String>();
			mapError.put("errorCode", YOSAN_SHIKKOU_TAISHOU_PATTERN_ERR[0]);
			mapError.put("errorMassage", String.format(YOSAN_SHIKKOU_TAISHOU_PATTERN_ERR[1], yosanShikkouTaishouNm));
			lstError.add(mapError);
		}
		
		//予算執行対象と「起案番号運用する/しない」の組み合わせチェック
		//「実施起案」「支出起案」設定時、「起案番号運用する/しない」が運用しないの場合エラー
		if ((yosanShikkouTaishou.equals(EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.JISSHI_KIAN) 
			||yosanShikkouTaishou.equals(EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_KIAN)) 
				&& kianBangouUnyouFlg.equals("0")){
			// 画面で指定した「予算執行対象」の名称を取得する。
			String yosanShikkouTaishouNm = systemLogic.findNaibuCdSetting("yosan_shikkou_taishou", yosanShikkouTaishou).get("name").toString();

			Map<String, String> mapError = new HashMap<String, String>();
			mapError.put("errorCode", YOSAN_SHIKKOU_TAISHOU_PATTERN_ERR[0]);
			mapError.put("errorMassage", "予算執行対象が"+yosanShikkouTaishouNm+"の場合は、起案番号運用する/しないのチェックをONにしてください。");
			lstError.add(mapError);
		}
		
		return lstError;
	}
	
	/**
	 * 予算執行対象組み合わせチェックの拡張メソッド【カスタマイズ向け】
	 * @param denpyouKbn 伝票区分
	 * @param yosanShikkouTaishou 予算執行対象
	 * @param isError エラー判定結果
	 * @return エラー判定結果
	 */
	public boolean isErrorPatternYosanShikkouTaishouExt(String denpyouKbn, String yosanShikkouTaishou, boolean isError){
		return isError;
	}
	
	/**
	 * ルート判定金額項目存在チェック<br>
	 * 伝票に指定した「ルート判定金額」値の入力部品がユーザー定義届書レイアウトに存在するかチェックする。<br>
	 * 
	 * @param denpyouKbn 伝票区分
	 * @param routeHanteiKingaku ルート判定金額
	 * @return エラーリスト
	 */
	public List<Map<String, String>> chkExistRouteHanteiKingaku(String denpyouKbn, String routeHanteiKingaku){
		List<Map<String, String>> lstError = new ArrayList<Map<String, String>>();

		/*
		 * 「ルート判定金額」の選択値を判定する。
		 */
		// 「分岐なし」の場合はチェック対象外のため処理を終了する。
		if (EteamNaibuCodeSetting.ROUTE_HANTEI_KINGAKU.BUNKI_NASHI.equals(routeHanteiKingaku)){
			return lstError;
		}

		/*
		 * 対象の伝票区分を判定する。
		 */
		// ワークフローイベント制御Logic
		WorkflowEventControlLogic wfEventLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, super.connection);
		// 伝票がユーザー定義届書以外の場合はチェック対象外のため処理を終了する。
		// 「ルート判定金額」に指定できる項目はユーザー定義届書にしか存在しないため
		if (!wfEventLogic.isKaniTodoke(denpyouKbn)){
			return lstError;
		}

		//  起票ナビカテゴリー内のSelect文を集約したLogic
		KihyouNaviCategoryLogic kihyouNaviLogic = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, super.connection);
		//  ユーザー定義届書Logic
		KaniTodokeLogic kaniTodokeLogic = EteamContainer.getComponent(KaniTodokeLogic.class, super.connection);

		// 「ルート判定金額」の選択値から予算執行項目IDを取得する。
		String[] idArrays = {null, null};
		switch (routeHanteiKingaku){
		case EteamNaibuCodeSetting.ROUTE_HANTEI_KINGAKU.RINGI_KINGAKU:
			// 稟議金額
			idArrays[0] = EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.RINGI_KINGAKU;
			break;
		case EteamNaibuCodeSetting.ROUTE_HANTEI_KINGAKU.SHISHUTSU_GOUKEI:
			// 支出金額合計
			idArrays[0] = EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_GOUKEI;
			break;
		case EteamNaibuCodeSetting.ROUTE_HANTEI_KINGAKU.SHISHUTSU_SHUUNYUU:
			// 収入又は支出
			idArrays[0] = EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHUUNYUU_GOUKEI;
			idArrays[1] = EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_GOUKEI;
			break;
		case EteamNaibuCodeSetting.ROUTE_HANTEI_KINGAKU.SHUUNYUU_GOUKEI:
			// 収入金額合計
			idArrays[0] = EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHUUNYUU_GOUKEI;
			break;
		default:
			break;
		}

		// 伝票区分に紐づくバージョンを取得する。
		GMap mapDnpSbtIcrn = kihyouNaviLogic.findDenpyouKanri(denpyouKbn);
		String version = mapDnpSbtIcrn.get("version").toString();

		// ユーザー定義届書レイアウトから「ルート判定金額」に指定された入力部品の件数を取得する。
		boolean isError = false;
		for (String aId : idArrays){
			if (null == aId){
				continue;
			}
			int cntParts = kaniTodokeLogic.chkExistInputParts(denpyouKbn, version, aId);
			if (0 == cntParts){
				// 対象の入力部品が存在しない場合はエラーで処理を終了する。
				isError = true;
				break;
			}
		}

		// エラーの場合はメッセージを設定する。
		if (isError){
			Map<String, String> mapError = new HashMap<String, String>();
			mapError.put("errorCode", ROUTE_HANTEI_KINGAKU_NOT_EXIST_ERR[0]);
			mapError.put("errorMassage", ROUTE_HANTEI_KINGAKU_NOT_EXIST_ERR[1]);
			lstError.add(mapError);
		}
		return lstError;
	}

	/**
	 * 予算執行対象項目存在チェック<br>
	 * 「支出部門」と「支出金額」の入力部品がユーザー定義届書レイアウトに存在するかチェックする。<br>
	 * 
	 * @param denpyouKbn 伝票区分
	 * @param yosanShikkouTaishou 画面で指定された予算執行対象
	 * @return エラーリスト
	 */
	public List<Map<String, String>> chkExistYosanShikkouTaishou(String denpyouKbn, String yosanShikkouTaishou){
		List<Map<String, String>> lstError = new ArrayList<Map<String, String>>();

		/*
		 * 画面で指定された予算執行対象を判定する。
		 */
		// 「対象外」が設定されている場合はチェック対象外のため処理を終了する。
		if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.TAISHOUGAI.equals(yosanShikkouTaishou)){
			return lstError;
		}

		/*
		 * 対象の伝票区分を判定する。
		 */
		// ワークフローイベント制御Logic
		WorkflowEventControlLogic wfEventLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, super.connection);
		// 伝票がユーザー定義届書以外の場合はチェック対象外のため処理を終了する。
		// 「支出部門」と「支出金額」の入力部品はユーザー定義届書にしか存在しないため
		if (!wfEventLogic.isKaniTodoke(denpyouKbn)){
			return lstError;
		}

		/*
		 * 既存の伝票種別一覧レコードを取得する。
		 */
		//  起票ナビカテゴリー内のSelect文を集約したLogic
		KihyouNaviCategoryLogic kihyouNaviLogic = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, super.connection);
		// 伝票区分に紐づくバージョンと予算執行対象項目を取得する。
		GMap mapDnpSbtIcrn = kihyouNaviLogic.findDenpyouKanri(denpyouKbn);
		String version = mapDnpSbtIcrn.get("version").toString();
		String preYosanShikkouTaishou = mapDnpSbtIcrn.get("yosan_shikkou_taishou").toString();
		// 前設定が「対象外」以外であった場合はチェック対象外のため処理を終了する。
		if (!EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.TAISHOUGAI.equals(preYosanShikkouTaishou)){
			return lstError;
		}

		/*
		 * 「支出部門」と「支出金額」の入力部品の存在を確認する。
		 */
		boolean isError = false;
		int cntParts = 0;
		StringBuilder sbKoumoku = new StringBuilder();
		//  ユーザー定義届書Logic
		KaniTodokeLogic kaniTodokeLogic = EteamContainer.getComponent(KaniTodokeLogic.class, super.connection);

		// ユーザー定義届書レイアウトから「支出部門」の入力部品件数を取得する。
		cntParts = kaniTodokeLogic.chkExistInputParts(denpyouKbn, version, EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_BUMON);
		if (0 == cntParts){
			// 対象の入力部品が存在する場合はエラーなしで処理を終了する。
			isError = true;
			sbKoumoku.append("支出部門");
		}

		// ユーザー定義届書レイアウトから「支出金額」の入力部品件数を取得する。
		cntParts = kaniTodokeLogic.chkExistInputParts(denpyouKbn, version, EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_KINGAKU);
		if (0 == cntParts){
			// 対象の入力部品が存在する場合はエラーなしで処理を終了する。
			isError = true;
			if (0 < sbKoumoku.length()){
				sbKoumoku.append("、");
			}
			sbKoumoku.append("支出金額");
		}

		// エラーの場合はメッセージを設定する。
		if (isError){
			Map<String, String> mapError = new HashMap<String, String>();
			mapError.put("errorCode", YOSAN_SHIKKOU_TAISHOU_NOT_EXIST_ERR[0]);
			mapError.put("errorMassage", String.format(YOSAN_SHIKKOU_TAISHOU_NOT_EXIST_ERR[1], sbKoumoku.toString()));
			lstError.add(mapError);
		}
		return lstError;
	}

	/**
	 * カラムデータ取得<br>
	 * 伝票種別一覧の伝票区分に合致したレコードから指定カラムのデータを取得する。<br>
	 * 
	 * @param denpyouKbn 伝票区分
	 * @param columnName カラム名
	 * @return カラムデータ
	 */
	protected String getColumnData(String denpyouKbn, String columnName){
		// 起票ナビカテゴリー内のSelect文を集約したLogic
		KihyouNaviCategoryLogic kihyouNaviLogic = EteamContainer.getComponent(KihyouNaviCategoryLogic.class, super.connection);

		// 指定された伝票区分のレコードを取得する。
		GMap map = kihyouNaviLogic.findDenpyouKanri(denpyouKbn);

		// 指定カラムのデータを取得して返却する。
		String result = map.get(columnName).toString();
		return result;
	}
	
	/**
	 * 起案番号運用取得<br>
	 * 指定された伝票区分に設定された「起案番号運用する／しない」値を取得する。
	 * 
	 * @param denpyouKbn 伝票区分
	 * @return 「起案番号運用する／しない」設定値
	 */
	public String getKianBangouUnyou(String denpyouKbn){
		// 起案番号運用フラグを取得して返却する。
		String result = this.getColumnData(denpyouKbn, KIAN_BANGOU_UNYOU_FLG);
		return result;
	}

	/**
	 * 予算執行対象取得<br>
	 * 指定された伝票区分に設定された「予算執行対象」値を取得する。
	 * 
	 * @param denpyouKbn 伝票区分
	 * @return 「予算執行対象」設定値
	 */
	public String getYosanShikkouTaishou(String denpyouKbn){
		// 予算執行対象を取得して返却する。
		String result = this.getColumnData(denpyouKbn, YOSAN_SHIKKOU_TAISHOU);
		return result;
	}

	/**
	 * ルート判定金額項目取得<br>
	 * 指定された伝票区分に設定された「ルート判定金額」値を取得する。
	 * 
	 * @param denpyouKbn 伝票区分
	 * @return 「ルート判定金額」設定値
	 */
	public String getRouteHanteiKingaku(String denpyouKbn){
		// ルート判定金額を取得して返却する。
		String result = this.getColumnData(denpyouKbn, ROUTE_HANTEI_KINGAKU);
		return result;
	}
}
