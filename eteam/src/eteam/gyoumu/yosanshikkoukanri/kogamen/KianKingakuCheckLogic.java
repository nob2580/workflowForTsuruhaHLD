package eteam.gyoumu.yosanshikkoukanri.kogamen;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.EteamLogger;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamSettingInfo;
import eteam.gyoumu.kihyounavi.DenpyouKanriLogic;
import eteam.gyoumu.workflow.WorkflowEventControlLogic;
import eteam.gyoumu.yosanshikkoukanri.YosanShikkouKanriCategoryLogic;

/**
 * 起案金額チェックダイアログLogic
 */
public class KianKingakuCheckLogic extends EteamAbstractLogic {
	/** ログ */
	protected EteamLogger log = EteamLogger.getLogger(KianKingakuCheckLogic.class);

	/** 起案チェック:判定コメント・予算内 */
	public final String cmntYosanNai = EteamSettingInfo.getSettingInfo(EteamSettingInfo.Key.KIAN_COMMENT_YOSANNAI);
	/** 起案チェック:判定コメント・超過 */
	public final String cmntChouka = setting.kianCommentChouka();

	/**
	 * 起案番号取得<br>
	 * 伝票に設定されている起案番号を取得する。<br>
	 * <ul>
	 * <li>実施起案の場合は実施起案番号</li>
	 * <li>支出起案の場合は支出起案番号</li>
	 * <li>支出依頼の場合は実施起案番号／支出起案番号</li>
	 * </ul>
	 * 
	 * @param denpyouKbn 伝票区分
	 * @param denpyouId 伝票ID
	 * @return 起案番号文字列
	 */
	public String getKianbangou(String denpyouKbn, String denpyouId){
		StringBuilder sb = new StringBuilder();

		/*
		 * 使用するロジッククラス
		 */

		// ワークフローイベント制御Logic
		WorkflowEventControlLogic wfecLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, super.connection);
		// 伝票管理Logic
		DenpyouKanriLogic dkLogic = EteamContainer.getComponent(DenpyouKanriLogic.class, super.connection);

		/*
		 * 伝票の伝票起案紐付レコードを取得する。
		 */
		GMap mapHimozuke = wfecLogic.selectDenpyouKianHimozuke(denpyouId);
		if (null == mapHimozuke){
			// レコードが存在しない場合はブランクを返却する。
			return sb.toString();
		}

		/*
		 * 伝票の予算執行対象により取得する起案番号を変える。
		 */
		String yosanShikkouTaishouCd = dkLogic.getYosanShikkouTaishou(denpyouKbn);

		// 実施起案、対象外、支出依頼の場合、実施起案番号を取得する。
		if (yosanShikkouTaishouCd.equals(EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.JISSHI_KIAN)
		 || yosanShikkouTaishouCd.equals(EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.TAISHOUGAI)
		 || yosanShikkouTaishouCd.equals(EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI)){
			if (null != mapHimozuke.get("jisshi_kian_bangou")){
				sb.append(mapHimozuke.get("jisshi_kian_bangou").toString());
			}
		}
		// 支出起案、支出依頼の場合、支出起案番号を取得する。
		if (yosanShikkouTaishouCd.equals(EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_KIAN)
		 || yosanShikkouTaishouCd.equals(EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI)){
			if (null != mapHimozuke.get("shishutsu_kian_bangou")){
				if (0 < sb.length()){
					sb.append("／");
				}
				sb.append(mapHimozuke.get("shishutsu_kian_bangou").toString());
			}
		}

		return sb.toString();
	}

	/**
	 * 初期表示データ取得<br>
	 * 
	 * @param denpyouId 伝票ID
	 * @param denpyouKbn 伝票区分
	 * @param nendo 年度
	 * @return 画面表示データマップ
	 */
	public List<GMap> loadData(String denpyouId, String denpyouKbn, String nendo){
		List<GMap> lstResult = new ArrayList<GMap>();

		/*
		 * 使用するロジッククラス
		 */

		// ワークフローイベント制御Logic
		WorkflowEventControlLogic wfEvntLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, super.connection);
		// 予算執行管理の共通Logic
		YosanShikkouKanriCategoryLogic commonLogic = EteamContainer.getComponent(YosanShikkouKanriCategoryLogic.class, super.connection);
		// 100倍
		BigDecimal oneHundred = BigDecimal.valueOf(100);
		
		/*
		 * 伝票から負担部門と金額を取得する。
		 */
		List<GMap> lstDenpyouInfo = new ArrayList<GMap>();
		if (wfEvntLogic.isKaniTodoke(denpyouKbn)){
			// ユーザー定義届書の場合
			String sqlStr = commonLogic.getKanitodokeSql(denpyouKbn, denpyouId).replace(":JOIN_KAMOKU_SECURITY", "");
			List<String> lstParam = new ArrayList<String>();
			lstParam.add(denpyouId);
			lstParam.add(denpyouId);
			lstParam.add(denpyouId);
			lstParam.add(denpyouId);
			lstDenpyouInfo = super.connection.load(sqlStr, lstParam.toArray());

		}else{
			// ユーザー定義届書以外の伝票から負担部門と金額を取得
			String sqlStr = null;
			switch (denpyouKbn){
			case EteamNaibuCodeSetting.DENPYOU_KBN.KEIHI_TATEKAE_SEISAN:
				// 経費立替精算
				sqlStr = commonLogic.getKeiseisanSql().replace(":JOIN_KAMOKU_SECURITY", "");
				lstDenpyouInfo = super.connection.load(sqlStr, denpyouId);
				break;
			case EteamNaibuCodeSetting.DENPYOU_KBN.SEIKYUUSHO_BARAI:
				// 請求書払い申請
				sqlStr = commonLogic.getSeikyuushobaraiSql().replace(":JOIN_KAMOKU_SECURITY", "");
				lstDenpyouInfo = super.connection.load(sqlStr, denpyouId);
				break;
			case EteamNaibuCodeSetting.DENPYOU_KBN.RYOHI_SEISAN:
				// 出張旅費精算（仮払精算）
				sqlStr = commonLogic.getRyohiseisanSql().replace(":JOIN_KAMOKU_SECURITY", "");
				lstDenpyouInfo = super.connection.load(sqlStr, denpyouId, denpyouId, denpyouId);
				break;
			case EteamNaibuCodeSetting.DENPYOU_KBN.KAIGAI_RYOHI_SEISAN:
				// 海外出張旅費精算（仮払精算） 
				sqlStr = commonLogic.getKaigaiRyohiseisanSql().replace(":JOIN_KAMOKU_SECURITY", "");
				lstDenpyouInfo = super.connection.load(sqlStr, denpyouId, denpyouId, denpyouId, denpyouId, denpyouId);
				break;
			case EteamNaibuCodeSetting.DENPYOU_KBN.SIHARAIIRAI:
				// 支払依頼申請
				sqlStr = commonLogic.getShiharaiIraiSql().replace(":JOIN_KAMOKU_SECURITY", "");
				lstDenpyouInfo = super.connection.load(sqlStr, denpyouId);
				break;
			default:
				lstDenpyouInfo = loadExtDenpyouInfo(denpyouKbn, denpyouId);
				break;
			}
		}

		// 伝票からデータが取得できない（対象外伝票も含む）場合は処理を終了する。
		if (0 == lstDenpyouInfo.size()){
			return null;
		}

		/*
		 * 伝票に起案添付された伝票情報を取得する。
		 */

		// 伝票に起案添付された伝票IDを取得する。
		GMap mapKianDenpyou = wfEvntLogic.selectDenpyouKianHimozuke(denpyouId);
		String kianDenpyouKbn = (String)mapKianDenpyou.get("kian_denpyou_kbn");
		String kianDenpyouId = (String)mapKianDenpyou.get("kian_denpyou");

		// 起案伝票から負担部門と金額を取得する。
		List<GMap> lstKianDenpyouInfo = new ArrayList<GMap>();
		if (!super.isEmpty(kianDenpyouId)){
			if (wfEvntLogic.isKaniTodoke(kianDenpyouKbn)){
				String sqlStr = commonLogic.getKanitodokeSql(kianDenpyouKbn, kianDenpyouId).replace(":JOIN_KAMOKU_SECURITY", "");
				List<String> lstParam = new ArrayList<String>();
				lstParam.add(kianDenpyouId);
				lstParam.add(kianDenpyouId);
				lstParam.add(kianDenpyouId);
				lstParam.add(kianDenpyouId);
				lstKianDenpyouInfo = super.connection.load(sqlStr, lstParam.toArray());

			}else{
				// ユーザー定義届書以外の伝票から負担部門と金額を取得
				String sqlStr = null;
				switch (kianDenpyouKbn){
				case EteamNaibuCodeSetting.DENPYOU_KBN.KARIBARAI_SHINSEI:
					// 仮払申請
					sqlStr = commonLogic.getKaribaraiSql();
					lstKianDenpyouInfo = super.connection.load(sqlStr, kianDenpyouId);
					break;
				case EteamNaibuCodeSetting.DENPYOU_KBN.RYOHI_KARIBARAI_SHINSEI:
					// 出張伺い申請（仮払申請）
					sqlStr = commonLogic.getRyohiKaribaraiSql().replace(":JOIN_KAMOKU_SECURITY", "");
					lstKianDenpyouInfo = super.connection.load(sqlStr, kianDenpyouId, kianDenpyouId, kianDenpyouId);
					break;
				case EteamNaibuCodeSetting.DENPYOU_KBN.KAIGAI_RYOHI_KARIBARAI_SHINSEI:
					// 海外出張伺い申請（仮払申請）
					sqlStr = commonLogic.getKaigaiRyohiKaribaraiSql().replace(":JOIN_KAMOKU_SECURITY", "");
					lstKianDenpyouInfo = super.connection.load(sqlStr, kianDenpyouId, kianDenpyouId, kianDenpyouId);
					break;
				default:
					lstKianDenpyouInfo = loadExtKianDenpyouInfo(kianDenpyouKbn, kianDenpyouId);
					break;
				}
			}
		}

		/*
		 * 伝票の起案番号に関連する支出依頼済みの伝票情報を取得する。
		 */

		// 支出依頼済みの伝票が存在する場合に伝票IDを取得する。
		List<GMap> lstShishutsuIraiZumiInfo = this.getShishutsuIraiZumiDenpyou(denpyouKbn, denpyouId);

		// 支出依頼済みの伝票から負担部門と金額を取得する。
		List<GMap> lstShishutsuIraiZumiDenpyouInfo = new ArrayList<GMap>();
		for (GMap aMap : lstShishutsuIraiZumiInfo){
			String zumiDenpyouId = aMap.get("denpyou_id").toString();
			String zumiDenpyouKbn = aMap.get("denpyou_kbn").toString();
			if (wfEvntLogic.isKaniTodoke(zumiDenpyouKbn)){
				String sqlStr = commonLogic.getKanitodokeSql(zumiDenpyouKbn, zumiDenpyouId).replace(":JOIN_KAMOKU_SECURITY", "");
				List<String> lstParam = new ArrayList<String>();
				lstParam.add(zumiDenpyouId);
				lstParam.add(zumiDenpyouId);
				lstParam.add(zumiDenpyouId);
				lstParam.add(zumiDenpyouId);
				lstShishutsuIraiZumiDenpyouInfo.addAll(super.connection.load(sqlStr, lstParam.toArray()));

			}else{
				// ユーザー定義届書以外の伝票から負担部門と金額を取得
				String sqlStr = null;
				switch (zumiDenpyouKbn){
				case EteamNaibuCodeSetting.DENPYOU_KBN.KEIHI_TATEKAE_SEISAN:
					// 経費立替精算
					sqlStr = commonLogic.getKeiseisanSql().replace(":JOIN_KAMOKU_SECURITY", "");
					lstShishutsuIraiZumiDenpyouInfo.addAll(super.connection.load(sqlStr, zumiDenpyouId));
					break;
				case EteamNaibuCodeSetting.DENPYOU_KBN.SEIKYUUSHO_BARAI:
					// 請求書払い申請
					sqlStr = commonLogic.getSeikyuushobaraiSql().replace(":JOIN_KAMOKU_SECURITY", "");
					lstShishutsuIraiZumiDenpyouInfo.addAll(super.connection.load(sqlStr, zumiDenpyouId));
					break;
				case EteamNaibuCodeSetting.DENPYOU_KBN.RYOHI_SEISAN:
					// 出張旅費精算（仮払精算）
					sqlStr = commonLogic.getRyohiseisanSql().replace(":JOIN_KAMOKU_SECURITY", "");
					lstShishutsuIraiZumiDenpyouInfo.addAll(super.connection.load(sqlStr, zumiDenpyouId, zumiDenpyouId, zumiDenpyouId));
					break;
				case EteamNaibuCodeSetting.DENPYOU_KBN.KAIGAI_RYOHI_SEISAN:
					// 海外出張旅費精算（仮払精算） 
					sqlStr = commonLogic.getKaigaiRyohiseisanSql().replace(":JOIN_KAMOKU_SECURITY", "");
					lstShishutsuIraiZumiDenpyouInfo.addAll(super.connection.load(sqlStr, zumiDenpyouId, zumiDenpyouId, zumiDenpyouId, zumiDenpyouId, zumiDenpyouId));
					break;
				case EteamNaibuCodeSetting.DENPYOU_KBN.SIHARAIIRAI:
					// 支払依頼申請
					sqlStr = commonLogic.getShiharaiIraiSql().replace(":JOIN_KAMOKU_SECURITY", "");
					lstShishutsuIraiZumiDenpyouInfo.addAll(super.connection.load(sqlStr, zumiDenpyouId));
					break;
				default:
					lstShishutsuIraiZumiDenpyouInfo.addAll(loadExtShishutsuIraiZumiDenpyouInfo(zumiDenpyouKbn, zumiDenpyouId));
					break;
				}
			}
		}

		/*
		 * データを集計する。
		 */

		// 伝票に登録された負担部門コードで集計部門を取得する。
		List<String> lstDuplicate = new ArrayList<String>();
		List<Map<String, String>> lstFutanBumonCd = new ArrayList<Map<String, String>>();
		for (GMap aBumon : lstDenpyouInfo){
			StringBuilder sb = new StringBuilder();
			Map<String, String> aMap = new HashMap<String, String>();

			// 負担部門
			String aFutanBumon = (String)aBumon.get("futan_bumon_cd");
			if (super.isEmpty(aFutanBumon)){
				continue;
			}
			aMap.put("futan_bumon_cd", aFutanBumon);
			sb.append(aFutanBumon);

			// 会社設定.予算執行.予算チェック粒度が 1:部門、科目単位 の場合
			// 科目と科目枝番を負担部門に紐付ける。
			if(commonLogic.isKamokuTani()){
				String aKamokuCd = (null == aBumon.get("kamoku_gaibu_cd")) ? "" : (String)aBumon.get("kamoku_gaibu_cd");
				String aKamokuNm = (null == aBumon.get("kamoku_name")) ? "" : (String)aBumon.get("kamoku_name");
				String aEdabanCd = (null == aBumon.get("edaban_code")) ? "" : (String)aBumon.get("edaban_code");
				String aEdabanNm = (null == aBumon.get("kamoku_edaban_name")) ? "" : (String)aBumon.get("kamoku_edaban_name");
				aMap.put("kamoku_gaibu_cd", aKamokuCd);
				aMap.put("kamoku_name", aKamokuNm);
				aMap.put("edaban_code", aEdabanCd);
				aMap.put("kamoku_edaban_name", aEdabanNm);
				// 重複判定にも科目と科目枝番を付加する。
				sb.append(aKamokuCd);
				sb.append(aEdabanCd);
			}

			// データに重複がなければ取得対象とする。
			if (!lstDuplicate.contains(sb.toString())){
				lstDuplicate.add(sb.toString());
				this.log.debug("■■■負担部門リスト:" + aMap.toString());
				lstFutanBumonCd.add(aMap);
			}
		}
		
		//伝票の伝票起案紐付レコードを取得する。
		GMap mapHimozuke = wfEvntLogic.selectDenpyouKianHimozuke(denpyouId);
		String nendoTmp = nendo;
		if(null==nendo || nendo.isEmpty()){
			nendoTmp = null==mapHimozuke.get("shishutsu_nendo")? mapHimozuke.get("jisshi_nendo").toString() : mapHimozuke.get("shishutsu_nendo").toString();
		}
		
		// 負担部門の集計部門を取得する。
		// このとき、負担部門に紐付けた科目と科目枝番を集計部門に移送する。
		List<GMap> lstShuukeiBumon = commonLogic.getShuukeiBumon(lstFutanBumonCd, nendoTmp);

		// 集計部門毎にデータを格納する。
		for (GMap aShuukeiBumon : lstShuukeiBumon){

			// サマリー行の各金額
			BigDecimal sKingaku = BigDecimal.valueOf(0);
			BigDecimal sJissekiGaku = BigDecimal.valueOf(0);
			BigDecimal sShinseiGaku = BigDecimal.valueOf(0);
			BigDecimal sZandaka = BigDecimal.valueOf(0);

			// サマリー行を設定する。
			GMap mapSummary = new GMap();
			mapSummary.put(YosanShikkouKanriCategoryLogic.MAP_ID.SYUUKEI_BUMON_CD, aShuukeiBumon.get("syuukei_bumon_cd"));
			mapSummary.put(YosanShikkouKanriCategoryLogic.MAP_ID.SYUUKEI_BUMON_NAME, aShuukeiBumon.get("syuukei_bumon_name"));
			if(commonLogic.isKamokuTani()){
				// 会社設定.予算執行.予算チェック粒度が 1:部門、科目単位 の場合、科目と科目枝番を設定する。
				mapSummary.put(YosanShikkouKanriCategoryLogic.MAP_ID.KAMOKU_CD, aShuukeiBumon.get("kamoku_gaibu_cd"));
				mapSummary.put(YosanShikkouKanriCategoryLogic.MAP_ID.KAMOKU_NAME, aShuukeiBumon.get("kamoku_name"));
				mapSummary.put(YosanShikkouKanriCategoryLogic.MAP_ID.KAMOKU_EDABAN_CD, aShuukeiBumon.get("edaban_code"));
				mapSummary.put(YosanShikkouKanriCategoryLogic.MAP_ID.KAMOKU_EDABAN_NAME, aShuukeiBumon.get("kamoku_edaban_name"));
			}else{
				mapSummary.put(YosanShikkouKanriCategoryLogic.MAP_ID.KAMOKU_CD, "");
				mapSummary.put(YosanShikkouKanriCategoryLogic.MAP_ID.KAMOKU_NAME, "");
				mapSummary.put(YosanShikkouKanriCategoryLogic.MAP_ID.KAMOKU_EDABAN_CD, "");
				mapSummary.put(YosanShikkouKanriCategoryLogic.MAP_ID.KAMOKU_EDABAN_NAME, "");
			}
			mapSummary.put(YosanShikkouKanriCategoryLogic.MAP_ID.MEISAI_BUMON_CD, "");
			mapSummary.put(YosanShikkouKanriCategoryLogic.MAP_ID.MEISAI_BUMON_NAME, "");
			mapSummary.put(YosanShikkouKanriCategoryLogic.MAP_ID.KIANGAKU, sKingaku);
			mapSummary.put(YosanShikkouKanriCategoryLogic.MAP_ID.JISSEKIGAKU, sJissekiGaku);
			mapSummary.put(YosanShikkouKanriCategoryLogic.MAP_ID.SHINSEIGAKU, sShinseiGaku);
			mapSummary.put(YosanShikkouKanriCategoryLogic.MAP_ID.ZANDAKA, sZandaka);
			mapSummary.put(YosanShikkouKanriCategoryLogic.MAP_ID.JUDGE_COMMENT, "");
			mapSummary.put(YosanShikkouKanriCategoryLogic.MAP_ID.RATE, 0);
			lstResult.add(mapSummary);

			// 集計部門に対する明細部門のリストを取得する。
			int kesn = (int)aShuukeiBumon.get("kesn");
			String shuukeiBumon = (String)aShuukeiBumon.get("syuukei_bumon_cd");
			List<GMap> lstMeisaiBumon = commonLogic.getMeisaiBumon(shuukeiBumon, kesn);

			// 明細行を設定する。
			for (GMap aDenpyou : lstDenpyouInfo){
				// 明細部門が集計部門に含まれるかを確認する。
				boolean isExist = false;
				GMap aMeisai = null;
				for (int i = 0; i < lstMeisaiBumon.size(); i++){
					aMeisai = lstMeisaiBumon.get(i);
					if(commonLogic.isKamokuTani()){
						// 会社設定.予算執行.予算チェック粒度が 1:部門、科目単位 の場合、負担部門の他、科目と科目枝番も同一かを確認する。
						if (aMeisai.get("futan_bumon_cd").equals(aDenpyou.get("futan_bumon_cd"))
						 && aShuukeiBumon.get("kamoku_gaibu_cd").equals(aDenpyou.get("kamoku_gaibu_cd"))
						 && aShuukeiBumon.get("edaban_code").equals(aDenpyou.get("edaban_code"))){
							isExist = true;
							break;
						}
					}else{
						if (aMeisai.get("futan_bumon_cd").equals(aDenpyou.get("futan_bumon_cd"))){
							isExist = true;
							break;
						}
					}
				}

				// 含まれている場合、サマリー行の後続として設定する。
				if (isExist){
					BigDecimal kingaku = BigDecimal.valueOf(0);
					BigDecimal jissekiGaku = BigDecimal.valueOf(0);
					BigDecimal shinseiGaku = new BigDecimal(aDenpyou.get("kingaku").toString());

					// 起案添付が存在する場合、同一負担部門の金額を設定する。
					// 伝票に起案添付は１つのみで、かつ金額は負担部門毎に合算されているので、そのまま値を設定
					for (int i = 0; i < lstKianDenpyouInfo.size(); i++){
						GMap aKianMeisai = lstKianDenpyouInfo.get(i);
						if(commonLogic.isKamokuTani()){
							// 会社設定.予算執行.予算チェック粒度が 1:部門、科目単位 の場合、負担部門の他、科目と科目枝番も同一かを確認する。
							if (aMeisai.get("futan_bumon_cd").equals(aKianMeisai.get("futan_bumon_cd"))
							 && aShuukeiBumon.get("kamoku_gaibu_cd").equals(aKianMeisai.get("kamoku_gaibu_cd"))
							 && aShuukeiBumon.get("edaban_code").equals(aKianMeisai.get("edaban_code"))){
								kingaku = new BigDecimal(aKianMeisai.get("kingaku").toString());
								break;
							}
						}else{
							if (aMeisai.get("futan_bumon_cd").equals(aKianMeisai.get("futan_bumon_cd"))){
								kingaku = new BigDecimal(aKianMeisai.get("kingaku").toString());
								break;
							}
						}
					}

					// 支出依頼済みの伝票が存在する場合、同一負担部門の金額を設定する。
					// 支出依頼済みの伝票は複数存在する場合があるので、負担部門毎に合算された金額を更に合算して設定
					for (int i = 0; i < lstShishutsuIraiZumiDenpyouInfo.size(); i++){
						GMap aZumiMeisai = lstShishutsuIraiZumiDenpyouInfo.get(i);
						if(commonLogic.isKamokuTani()){
							// 会社設定.予算執行.予算チェック粒度が 1:部門、科目単位 の場合、負担部門の他、科目と科目枝番も同一かを確認する。
							if (aMeisai.get("futan_bumon_cd").equals(aZumiMeisai.get("futan_bumon_cd"))
							 && aShuukeiBumon.get("kamoku_gaibu_cd").equals(aZumiMeisai.get("kamoku_gaibu_cd"))
							 && aShuukeiBumon.get("edaban_code").equals(aZumiMeisai.get("edaban_code"))){
								jissekiGaku = jissekiGaku.add(new BigDecimal(aZumiMeisai.get("kingaku").toString()));
							}
						}else{
							if (aMeisai.get("futan_bumon_cd").equals(aZumiMeisai.get("futan_bumon_cd"))){
								jissekiGaku = jissekiGaku.add(new BigDecimal(aZumiMeisai.get("kingaku").toString()));
							}
						}
					}

					// 起案残高を計算する。
					BigDecimal zandaka = kingaku.subtract(jissekiGaku.add(shinseiGaku));

					GMap mapMeisai = new GMap();
					mapMeisai.put(YosanShikkouKanriCategoryLogic.MAP_ID.SYUUKEI_BUMON_CD, aShuukeiBumon.get("syuukei_bumon_cd"));
					mapMeisai.put(YosanShikkouKanriCategoryLogic.MAP_ID.SYUUKEI_BUMON_NAME, aShuukeiBumon.get("syuukei_bumon_name"));
					if(commonLogic.isKamokuTani()){
						// 会社設定.予算執行.予算チェック粒度が 1:部門、科目単位 の場合、科目と科目枝番を設定する。
						mapMeisai.put(YosanShikkouKanriCategoryLogic.MAP_ID.KAMOKU_CD, (null == aDenpyou.get("kamoku_gaibu_cd")) ? "" : aDenpyou.get("kamoku_gaibu_cd"));
						mapMeisai.put(YosanShikkouKanriCategoryLogic.MAP_ID.KAMOKU_NAME, (null == aDenpyou.get("kamoku_name")) ? "" : aDenpyou.get("kamoku_name"));
						mapMeisai.put(YosanShikkouKanriCategoryLogic.MAP_ID.KAMOKU_EDABAN_CD, (null == aDenpyou.get("edaban_code")) ? "" : aDenpyou.get("edaban_code"));
						mapMeisai.put(YosanShikkouKanriCategoryLogic.MAP_ID.KAMOKU_EDABAN_NAME, (null == aDenpyou.get("kamoku_edaban_name")) ? "" : aDenpyou.get("kamoku_edaban_name"));
					}else{
						mapMeisai.put(YosanShikkouKanriCategoryLogic.MAP_ID.KAMOKU_CD, "");
						mapMeisai.put(YosanShikkouKanriCategoryLogic.MAP_ID.KAMOKU_NAME, "");
						mapMeisai.put(YosanShikkouKanriCategoryLogic.MAP_ID.KAMOKU_EDABAN_CD, "");
						mapMeisai.put(YosanShikkouKanriCategoryLogic.MAP_ID.KAMOKU_EDABAN_NAME, "");
					}
					mapMeisai.put(YosanShikkouKanriCategoryLogic.MAP_ID.MEISAI_BUMON_CD, aMeisai.get("futan_bumon_cd"));
					mapMeisai.put(YosanShikkouKanriCategoryLogic.MAP_ID.MEISAI_BUMON_NAME, aMeisai.get("futan_bumon_name"));
					mapMeisai.put(YosanShikkouKanriCategoryLogic.MAP_ID.KIANGAKU, kingaku);
					mapMeisai.put(YosanShikkouKanriCategoryLogic.MAP_ID.JISSEKIGAKU, jissekiGaku);
					mapMeisai.put(YosanShikkouKanriCategoryLogic.MAP_ID.SHINSEIGAKU, shinseiGaku);
					mapMeisai.put(YosanShikkouKanriCategoryLogic.MAP_ID.ZANDAKA, zandaka);
					mapMeisai.put(YosanShikkouKanriCategoryLogic.MAP_ID.JUDGE_COMMENT, "");
					int rate = oneHundred.intValue() + 1;
					if (!kingaku.equals(BigDecimal.valueOf(0))){
						rate = jissekiGaku.add(shinseiGaku).divide(kingaku, 2, RoundingMode.UP).multiply(oneHundred).intValue();
					}
					mapMeisai.put(YosanShikkouKanriCategoryLogic.MAP_ID.RATE, rate);
					lstResult.add(mapMeisai);

					// サマリー行の各金額を設定する。
					sKingaku = sKingaku.add(kingaku);
					sJissekiGaku = sJissekiGaku.add(jissekiGaku);
					sShinseiGaku = sShinseiGaku.add(shinseiGaku);
					sZandaka = sZandaka.add(zandaka);
				}
			}

			// 集計部門のデータを再設定する。
			mapSummary.put(YosanShikkouKanriCategoryLogic.MAP_ID.KIANGAKU, sKingaku);
			mapSummary.put(YosanShikkouKanriCategoryLogic.MAP_ID.JISSEKIGAKU, sJissekiGaku);
			mapSummary.put(YosanShikkouKanriCategoryLogic.MAP_ID.SHINSEIGAKU, sShinseiGaku);
			mapSummary.put(YosanShikkouKanriCategoryLogic.MAP_ID.ZANDAKA, sZandaka);
			int rateSummary = oneHundred.intValue() + 1;
			if (!sKingaku.equals(BigDecimal.valueOf(0))){
				rateSummary = sJissekiGaku.add(sShinseiGaku).divide(sKingaku, 2, RoundingMode.UP).multiply(oneHundred).intValue();
			}
			mapSummary.put(YosanShikkouKanriCategoryLogic.MAP_ID.RATE, rateSummary);
		}

		return lstResult;
	}

	/**
	 * 支出依頼済み伝票取得<br>
	 * 伝票に紐づく支出依頼伝票が支出依頼済みと扱う伝票IDと伝票区分を取得する。<br>
	 * 
	 * @param denpyouKbn 伝票区分
	 * @param denpyouId 伝票ID
	 * @return 支出依頼済み伝票情報マップリスト（マップキー：denpyou_id, denpyou_kbn）
	 */
	protected List<GMap> getShishutsuIraiZumiDenpyou(String denpyouKbn, String denpyouId){
		List<GMap> lstResult = new ArrayList<GMap>();

		// 伝票管理Logic
		DenpyouKanriLogic dkLogic = EteamContainer.getComponent(DenpyouKanriLogic.class, super.connection);
		// ワークフローイベント制御Logic
		WorkflowEventControlLogic wfecLogic = EteamContainer.getComponent(WorkflowEventControlLogic.class, super.connection);

		/*
		 * 伝票の予算執行対象を取得する。
		 */
		String yosanShikkouTaishouCd = dkLogic.getYosanShikkouTaishou(denpyouKbn);

		/*
		 * 起案追加された伝票の予算執行対象を取得する。
		 */
		GMap mapHimozuke = wfecLogic.selectDenpyouKianHimozuke(denpyouId);
		if (null == mapHimozuke){
			// レコードが存在しない場合は空リストを返却する。
			return lstResult;
		}
		String kianDenpyouKbn = (String)mapHimozuke.get("kian_denpyou_kbn");
		if (super.isEmpty(kianDenpyouKbn)){
			// 起案追加されていない場合は空リストを返却する。
			return lstResult;
		}
		/*
		 * 支出依頼済み伝票を取得する。
		 */
		if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_KIAN.equals(yosanShikkouTaishouCd)){
			// 伝票が支出起案の場合、起案追加された伝票は実施起案となる。
			lstResult = this.getZumiDenpyouForShishutsuKian(denpyouId);

		}else if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI.equals(yosanShikkouTaishouCd)){
			// 伝票が支出依頼の場合
			lstResult = this.getZumiDenpyouForShishutsuIraiKian(denpyouId); // 起案が支出が実施かは関係ないことが判明
		}

		return lstResult;
	}

	/**
	 * 支出依頼済み伝票取得（支出起案用）<br>
	 * 伝票に紐づく実施起案番号を取得して、下記の条件のいずれかに合致する伝票情報を取得する。<br>
	 * <ul>
	 * <li>同じ起案伝票IDを持つ支出依頼で、伝票状態が「申請中」または「承認済み」の伝票。</li>
	 * <li>『同じ実施起案番号を持つ支出起案伝票で採番された支出起案番号』を持つ支出依頼で、伝票状態が「起案中」の伝票。</li>
	 * </ul>
	 * 
	 * @param denpyouId 伝票ID
	 * @return 支出依頼済み伝票情報マップリスト（マップキー：denpyou_id, denpyou_kbn）
	 */
	protected List<GMap> getZumiDenpyouForShishutsuKian(String denpyouId){

		// Main SQL
		StringBuffer sbSql = this.appendSqlForJisshiKianBangouAndSearch1(denpyouId)
			// 支出起案の抽出
			.append(" ,SHISHUTSU_KIAN AS (")
			 .append("  SELECT")
			 .append("    A.denpyou_id")
			 .append("  FROM denpyou_kian_himozuke AS A")
			 .append("  INNER JOIN JISSHI_KIAN_BANGOU AS B ON B.kian_denpyou = A.kian_denpyou")// 起案番号一致だと年度をまたいだ場合などに挙動がおかしくなるので、起案伝票ID一致をみる
			 .append("  INNER JOIN denpyou AS C ON C.denpyou_id = A.denpyou_id")
			 .append("  INNER JOIN denpyou_shubetsu_ichiran AS D ON D.denpyou_kbn = C.denpyou_kbn")
			 .append("  WHERE C.denpyou_joutai = ? AND D.yosan_shikkou_taishou = ?")
			.append(" )")
			// そこから対象の支出依頼を抽出
			 .append(" ,SEARCH2 AS (")
			 .append("  SELECT")
			 .append("    A.denpyou_id")
			 .append("   ,F.denpyou_kbn")
			 .append("  FROM denpyou_kian_himozuke AS A")
			 .append("  INNER JOIN denpyou AS F ON F.denpyou_id = A.denpyou_id")
			 .append("  INNER JOIN denpyou_shubetsu_ichiran AS G ON G.denpyou_kbn = F.denpyou_kbn")
			 .append("    WHERE F.denpyou_joutai = ? AND G.yosan_shikkou_taishou = ?")
			 .append("    AND A.denpyou_id != ? AND EXISTS (SELECT * FROM SHISHUTSU_KIAN H WHERE H.denpyou_id = A.kian_denpyou)") // 自分自身を除く
			 .append(" )")
			 .append(" SELECT * FROM SEARCH1")
			 .append(" UNION ALL")
			 .append(" SELECT * FROM SEARCH2");

		/*
		 * 検索条件を指定する。
		 */
		List<Object> param = new ArrayList<Object>();

		// :COND_IN用
		StringBuffer sbSearch1Joutai = new StringBuffer();
		sbSearch1Joutai.append("'").append(EteamNaibuCodeSetting.DENPYOU_JYOUTAI.SHINSEI_CHUU).append("'");
		sbSearch1Joutai.append(",");
		sbSearch1Joutai.append("'").append(EteamNaibuCodeSetting.DENPYOU_JYOUTAI.SYOUNIN_ZUMI).append("'");

		// パラメータを設定する。
		// 共通部分用
		param.add(denpyouId);
		param.add(EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI);
		param.add(denpyouId);
		// SEARCH2用
		param.add(EteamNaibuCodeSetting.DENPYOU_JYOUTAI.SYOUNIN_ZUMI);
		param.add(EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_KIAN);
		param.add(EteamNaibuCodeSetting.DENPYOU_JYOUTAI.KIHYOU_CHUU);
		param.add(EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI);
		param.add(denpyouId);

		/*
		 * SQLを実行する。
		 */
		String strSql = sbSql.toString().replace(":COND_IN", sbSearch1Joutai.toString());
		return connection.load(strSql, param.toArray());
	}
	
	/**
	 * 支出依頼済み伝票取得（支出依頼－起案用）<br>
	 * 伝票に紐づく起案伝票IDを取得して、下記の条件に合致する伝票情報を取得する。<br>
	 * <ul>
	 * <li>同起案伝票IDを持つ支出依頼で、伝票状態が「申請中」または「承認済み」の伝票。</li>
	 * </ul>
	 * @param denpyouId 伝票ID
	 * @return 支出依頼済み伝票情報マップリスト（マップキー：denpyou_id, denpyou_kbn）
	 */
	protected List<GMap> getZumiDenpyouForShishutsuIraiKian(String denpyouId)
	{
		// Main SQL
		StringBuffer sbSql = this.appendSqlForJisshiKianBangouAndSearch1(denpyouId)
			 .append(" SELECT * FROM SEARCH1");

		/*
		 * 検索条件を指定する。
		 */
		List<Object> param = new ArrayList<Object>();

		// :COND_IN用
		StringBuffer sbSearch1Joutai = new StringBuffer();
		sbSearch1Joutai.append("'").append(EteamNaibuCodeSetting.DENPYOU_JYOUTAI.SHINSEI_CHUU).append("'");
		sbSearch1Joutai.append(",");
		sbSearch1Joutai.append("'").append(EteamNaibuCodeSetting.DENPYOU_JYOUTAI.SYOUNIN_ZUMI).append("'");

		// パラメータを設定する。
		param.add(denpyouId);
		param.add(EteamNaibuCodeSetting.YOSAN_SHIKKOU_TAISHOU.SHISHUTSU_IRAI);
		param.add(denpyouId);

		/*
		 * SQLを実行する。
		 */
		String strSql = sbSql.toString().replace(":COND_IN", sbSearch1Joutai.toString());
		return connection.load(strSql, param.toArray());
	}
	
	/**
	 * @param denpyouId 伝票ID
	 * @return Search1までのWITH文共通部分
	 */
	protected StringBuffer appendSqlForJisshiKianBangouAndSearch1(String denpyouId)
	{
		return new StringBuffer()
		 .append("WITH")
		 .append(" JISSHI_KIAN_BANGOU AS (")
		 .append("  SELECT AX.kian_denpyou")
		 .append("  FROM denpyou_kian_himozuke AS AX")
		 .append("  WHERE AX.denpyou_id = ? AND LENGTH(COALESCE(AX.kian_denpyou, '')) > 0")
		 .append(" )")
		 .append(" ,SEARCH1 AS (")
		 .append("  SELECT")
		 .append("    A.denpyou_id")
		 .append("   ,C.denpyou_kbn")
		 .append("  FROM denpyou_kian_himozuke AS A")
		 .append("  INNER JOIN JISSHI_KIAN_BANGOU AS B ON B.kian_denpyou = A.kian_denpyou") // 起案番号一致だと年度をまたいだ場合などに挙動がおかしくなるので、起案伝票ID一致をみる
		 .append("  INNER JOIN denpyou AS C ON C.denpyou_id = A.denpyou_id")
		 .append("  INNER JOIN denpyou_shubetsu_ichiran AS D ON D.denpyou_kbn = C.denpyou_kbn")
		 .append("  WHERE C.denpyou_joutai IN (:COND_IN) AND D.yosan_shikkou_taishou = ?")
		 .append("    AND A.denpyou_id != ?") // 自分自身以外
		 .append(" )");
	}
	
	/**
	 * 申請伝票情報取得（カスタマイズ用）
	 * @param denpyouKbn 伝票区分
	 * @param denpyouId 伝票ID
	 * @return パッケージ版では空。拡張版では追加した伝票の情報を取得する
	 */
	public List<GMap> loadExtDenpyouInfo(String denpyouKbn, String denpyouId) {
		return null;
	}
	
	/**
	 * 起案伝票情報取得（カスタマイズ用）
	 * @param denpyouKbn 伝票区分
	 * @param denpyouId 伝票ID
	 * @return パッケージ版では空。拡張版では追加した伝票の情報を取得する
	 */
	public List<GMap> loadExtKianDenpyouInfo(String denpyouKbn, String denpyouId) {
		return null;
	}
	
	/**
	 * 支出依頼伝票情報取得（カスタマイズ用）
	 * @param denpyouKbn 伝票区分
	 * @param denpyouId 伝票ID
	 * @return パッケージ版では空。拡張版では追加した伝票の情報を取得する
	 */
	public List<GMap> loadExtShishutsuIraiZumiDenpyouInfo(String denpyouKbn, String denpyouId) {
		return null;
	}
}
