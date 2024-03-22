package eteam.gyoumu.kanitodoke;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eteam.base.EteamAbstractLogic;
import eteam.base.EteamContainer;
import eteam.base.EteamFormatter;
import eteam.base.GMap;
import eteam.common.EteamNaibuCodeSetting;
import eteam.common.EteamNaibuCodeSetting.AREA_KBN;
import eteam.common.EteamNaibuCodeSetting.BUHIN_FORMAT;
import eteam.common.select.KaniTodokeCategoryLogic;
import eteam.database.dao.DenpyouShubetsuIchiranDao;
import eteam.database.dao.KaniTodokeCheckboxDao;
import eteam.database.dao.KaniTodokeKoumokuDao;
import eteam.database.dao.KaniTodokeListKoDao;
import eteam.database.dao.KaniTodokeListOyaDao;
import eteam.database.dao.KaniTodokeMasterDao;
import eteam.database.dao.KaniTodokeSelectItemDao;
import eteam.database.dao.KaniTodokeTextDao;
import eteam.database.dao.KaniTodokeTextareaDao;
import eteam.database.dao.KaniTodokeVersionDao;
import eteam.database.dto.DenpyouShubetsuIchiran;
import eteam.database.dto.KaniTodokeCheckbox;
import eteam.database.dto.KaniTodokeKoumoku;
import eteam.database.dto.KaniTodokeListKo;
import eteam.database.dto.KaniTodokeListOya;
import eteam.database.dto.KaniTodokeMaster;
import eteam.database.dto.KaniTodokeSelectItem;
import eteam.database.dto.KaniTodokeText;
import eteam.database.dto.KaniTodokeTextarea;
import eteam.database.dto.KaniTodokeVersion;
import eteam.gyoumu.kanitodoke.KaniTodokeConst.BuhinType;
import eteam.gyoumu.kanitodoke.KaniTodokeConst.DataListIndex;
import eteam.gyoumu.kanitodoke.KaniTodokeConst.HtmlCreateMode;
import eteam.gyoumu.kanitodoke.KaniTodokeConst.MasterKbn;
import eteam.gyoumu.kanitodoke.KaniTodokeConst.OptionListIndex;
import eteam.gyoumu.user.User;

/**
 * ユーザー定義届書Logic
 */
public class KaniTodokeLogic extends EteamAbstractLogic {

	/**
	 * @param denpyouId 伝票ID
	 * @param denpyouKbn 伝票区分
	 * @param version バージョン
	 * @param gamenInfo 画面情報
	 * @param userId ユーザーID
	 * @return 登録件数
	 */
	public int insertShinsei(String denpyouId, String denpyouKbn, String version, KaniTodokeGamenInfo gamenInfo, String userId) {

		int ret = 0;
		
		// 登録データ 
		Map<Integer, Map<String, String>> mapValue1 = gamenInfo.getValue1();
		Map<Integer, Map<String, String>> mapValue2 = gamenInfo.getValue2();
		 
		for (Integer denpyou_edano : mapValue1.keySet()) {

			Map<String, String> mapValue = mapValue1.get(denpyou_edano);

			for (String item_name : mapValue.keySet()) {

				String value1 = mapValue1.get(denpyou_edano).get(item_name);
				String value2 = mapValue2.get(denpyou_edano).get(item_name);
				
				if (value1 == null) {
					value1 = "";
				}
				
				if (value2 == null) {
					value2 = "";
				}
				
				final String sql = "INSERT INTO kani_todoke VALUES(?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
				ret += connection.update(sql, denpyouId, denpyouKbn, Integer.parseInt(version), item_name, value1, value2, userId, userId);
			}
		}
		
		return ret;
	}
	
	/**
	 * @param denpyouId 伝票ID
	 * @param gamenInfo 画面情報
	 * @param userId ユーザーID
	 * @return 登録件数
	 */
	public int updateShinsei(String denpyouId, KaniTodokeGamenInfo gamenInfo, String userId) {
		
		int ret = 0;
		
		// 登録データ 
		Map<Integer, Map<String, String>> mapValue1 = gamenInfo.getValue1();
		Map<Integer, Map<String, String>> mapValue2 = gamenInfo.getValue2();
		 
		for (Integer denpyou_edano : mapValue1.keySet()) {

			Map<String, String> mapValue = mapValue1.get(denpyou_edano);

			for (String item_name : mapValue.keySet()) {

				String value1 = mapValue1.get(denpyou_edano).get(item_name);
				String value2 = mapValue2.get(denpyou_edano).get(item_name);
				
				if (value1 == null) {
					value1 = "";
				}
				
				if (value2 == null) {
					value2 = "";
				}
				
				final String sql = "UPDATE kani_todoke SET value1 = ?, value2 = ?, koushin_user_id = ?, koushin_time = current_timestamp WHERE denpyou_id = ? AND item_name = ?";
				ret += connection.update(sql, value1, value2, userId, denpyouId, item_name);
			}
		}
		
		return ret;
	}
	
	/**
	 * @param denpyouId 伝票ID
	 * @param gamenInfo 画面情報
	 * @param userId ユーザーID
	 * @return 更新件数
	 */
	public int insertMeisai(String denpyouId, KaniTodokeGamenInfo gamenInfo, String userId) {
		
		int ret = 0;
		
		// 登録データ 
		Map<Integer, Map<String, String>> mapValue1 = gamenInfo.getValue1();
		Map<Integer, Map<String, String>> mapValue2 = gamenInfo.getValue2();
		 
		for (Integer denpyou_edano : mapValue1.keySet()) {

			Map<String, String> mapValue = mapValue1.get(denpyou_edano);

			for (String item_name : mapValue.keySet()) {

				String value1 = mapValue1.get(denpyou_edano).get(item_name);
				String value2 = mapValue2.get(denpyou_edano).get(item_name);
				
				if (value1 == null) {
					value1 = "";
				}
				
				if (value2 == null) {
					value2 = "";
				}
				
				final String sql = "INSERT INTO kani_todoke_meisai VALUES(?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
				ret += connection.update(sql, denpyouId, denpyou_edano, item_name, value1, value2, userId, userId);
			}
		}
		
		return ret;
	}

	/**
	 * 伝票配下の明細レコードを全て削除する
	 * @param denpyouId 伝票ID
	 * @return 処理件数
	 */
	public int deleteMeisai(String denpyouId) {
		final String sql = "DELETE FROM kani_todoke_meisai WHERE denpyou_id = ?";
		return connection.update(sql, denpyouId);
	}

	/**
	 * ユーザー定義届書サマリに登録
	 * @param denpyouId 伝票ID
	 * @param denpyouKbn 伝票区分
	 * @param version バージョン
	 * @param gamenInfo 画面情報（申請部）
	 * @param gamenMeisaiInfo 画面情報（明細部）
	 * @return 結果
	 */
	public int insertSummary(String denpyouId, String denpyouKbn, String version, KaniTodokeGamenInfo gamenInfo, KaniTodokeGamenInfo gamenMeisaiInfo) {

		int ret = 0;
		
		//サマリーに保存する項目のitem_nameを取得
		String itemNameRingiKingaku           = getYosanShikkouItemName(denpyouKbn, version, "ringi_kingaku");
		String itemNameShishutsuKingakuGoukei = getYosanShikkouItemName(denpyouKbn, version, "shishutsu_kingaku_goukei");
		String itemNameShuunyuuKingakuGoukei  = getYosanShikkouItemName(denpyouKbn, version, "shuunyuu_kingaku_goukei");
		String itemNameKenmei                 = getYosanShikkouItemName(denpyouKbn, version, "kenmei");
		String itemNameNaiyouShinsei          = getYosanShikkouItemName(denpyouKbn, version, "naiyou_shinsei");
		String itemNameNaiyouMeisai           = getYosanShikkouItemName(denpyouKbn, version, "naiyou_meisai");
		
		//保存項目の値を格納する変数
		BigDecimal ringiKingaku = null;
		BigDecimal shishutsuKingakuGoukei = null;
		BigDecimal shuunyuuKingakuGoukei = null;
		String kenmei = "";
		String naiyou = "";
		
		// 申請部データ
		Map<Integer, Map<String, String>> mapValue1 = gamenInfo.getValue1();
		
		for (Integer denpyou_edano : mapValue1.keySet()) {

			Map<String, String> mapValue = mapValue1.get(denpyou_edano);

			for (String item_name : mapValue.keySet()) {
				
				String value1 = mapValue1.get(denpyou_edano).get(item_name);
				if (!super.isEmpty(value1)) {
					
					//稟議金額
					if(item_name.equals(itemNameRingiKingaku)){
						ringiKingaku = new BigDecimal(value1.replaceAll("," , ""));
					
					//支出金額合計
					}else if(item_name.equals(itemNameShishutsuKingakuGoukei)){
						shishutsuKingakuGoukei = new BigDecimal(value1.replaceAll("," , ""));
					
					//収入金額合計
					}else if(item_name.equals(itemNameShuunyuuKingakuGoukei)){
						shuunyuuKingakuGoukei = new BigDecimal(value1.replaceAll("," , ""));
						
					//件名
					}else if(item_name.equals(itemNameKenmei)){
						kenmei = value1;
						
					//内容
					}else if(item_name.equals(itemNameNaiyouShinsei)){
						naiyou = value1;
					}
				}
			}
		}
		
		//明細部データ
		Map<Integer, Map<String, String>> mapMeisaiValue1 = gamenMeisaiInfo.getValue1();
		
		for (Integer denpyou_edano : mapMeisaiValue1.keySet()) {

			Map<String, String> mapValue = mapMeisaiValue1.get(denpyou_edano);
			
			if(1 == denpyou_edano){
				for (String item_name : mapValue.keySet()) {
					
					String value1 = mapMeisaiValue1.get(denpyou_edano).get(item_name);
					if (value1 != null) {
						
						//内容
						if(item_name.equals(itemNameNaiyouMeisai)){
							if("".equals(itemNameNaiyouShinsei)){ naiyou = value1; }
						}
					}
				}
			}
		}
		
		final String sql = "INSERT INTO kani_todoke_summary VALUES(?,?,?,?,?,?)";
		ret += connection.update(sql, denpyouId, ringiKingaku, shishutsuKingakuGoukei, shuunyuuKingakuGoukei, kenmei, naiyou);
		return ret;
	}

	/**
	 * ユーザー定義届書サマリを更新
	 * @param denpyouId 伝票ID
	 * @param denpyouKbn 伝票区分
	 * @param version バージョン
	 * @param gamenInfo 画面情報(申請部）
	 * @param gamenMeisaiInfo 画面情報(明細部)
	 * @return 結果
	 */
	public int updateSummary(String denpyouId, String denpyouKbn, String version, KaniTodokeGamenInfo gamenInfo, KaniTodokeGamenInfo gamenMeisaiInfo) {
		int ret = 0;
		
		//サマリーに保存する項目のitem_nameを取得
		String itemNameRingiKingaku           = getYosanShikkouItemName(denpyouKbn, version, "ringi_kingaku");
		String itemNameShishutsuKingakuGoukei = getYosanShikkouItemName(denpyouKbn, version, "shishutsu_kingaku_goukei");
		String itemNameShuunyuuKingakuGoukei  = getYosanShikkouItemName(denpyouKbn, version, "shuunyuu_kingaku_goukei");
		String itemNameKenmei                 = getYosanShikkouItemName(denpyouKbn, version, "kenmei");
		String itemNameNaiyouShinsei          = getYosanShikkouItemName(denpyouKbn, version, "naiyou_shinsei");
		String itemNameNaiyouMeisai           = getYosanShikkouItemName(denpyouKbn, version, "naiyou_meisai");
		
		//保存項目の値を格納する変数
		BigDecimal ringiKingaku = null;
		BigDecimal shishutsuKingakuGoukei = null;
		BigDecimal shuunyuuKingakuGoukei = null;
		String kenmei = "";
		String naiyou = "";
		
		// 申請部データ
		Map<Integer, Map<String, String>> mapValue1 = gamenInfo.getValue1();
		
		for (Integer denpyou_edano : mapValue1.keySet()) {

			Map<String, String> mapValue = mapValue1.get(denpyou_edano);

			for (String item_name : mapValue.keySet()) {
				
				String value1 = mapValue1.get(denpyou_edano).get(item_name);
				if (!super.isEmpty(value1)) {
					
					//稟議金額
					if(item_name.equals(itemNameRingiKingaku)){
						ringiKingaku = new BigDecimal(value1.replaceAll("," , ""));
					
					//支出金額合計
					}else if(item_name.equals(itemNameShishutsuKingakuGoukei)){
						shishutsuKingakuGoukei = new BigDecimal(value1.replaceAll("," , ""));
					
					//収入金額合計
					}else if(item_name.equals(itemNameShuunyuuKingakuGoukei)){
						shuunyuuKingakuGoukei = new BigDecimal(value1.replaceAll("," , ""));
						
					//件名
					}else if(item_name.equals(itemNameKenmei)){
						kenmei = value1;
						
					//内容
					}else if(item_name.equals(itemNameNaiyouShinsei)){
						naiyou = value1;
					}
				}
			}
		}
		
		//明細部データ
		Map<Integer, Map<String, String>> mapMeisaiValue1 = gamenMeisaiInfo.getValue1();
		
		for (Integer denpyou_edano : mapMeisaiValue1.keySet()) {

			Map<String, String> mapValue = mapMeisaiValue1.get(denpyou_edano);
			
			if(1 == denpyou_edano){
				for (String item_name : mapValue.keySet()) {
					
					String value1 = mapMeisaiValue1.get(denpyou_edano).get(item_name);
					if (value1 != null) {
						
						//内容
						if(item_name.equals(itemNameNaiyouMeisai)){
							if("".equals(itemNameNaiyouShinsei)){ naiyou = value1; }
						}
					}
				}
			}
		}
		
		final String sql = "UPDATE kani_todoke_summary SET ringi_kingaku=?,shishutsu_kingaku_goukei=?,shuunyuu_kingaku_goukei=?,kenmei=?,naiyou=? WHERE denpyou_id=?";
		ret += connection.update(sql, ringiKingaku, shishutsuKingakuGoukei, shuunyuuKingakuGoukei, kenmei, naiyou, denpyouId);
		return ret;
	}

	/**
	 * 請求書払い申請の支払日を更新する。
	 * @param denpyouId  伝票ID
	 * @param keijoubi   計上日
	 * @param shiharaibi 支払日
	 * @param masrefFlg  マスター参照フラグ
	 * @param userId     ユーザーID
	 * @return 処理件数
	 */
	public int updateShiharaibi(
			String denpyouId,
			Date keijoubi,
			Date shiharaibi,
			String masrefFlg,
			String userId
	) {
		final String sql =
				"UPDATE seikyuushobarai "
			+   "SET keijoubi = ?, shiharaibi = ?, masref_flg = ?, koushin_user_id=?, koushin_time=current_timestamp "
			+ "WHERE denpyou_id = ?";
		return connection.update(sql, keijoubi, shiharaibi, masrefFlg, userId, denpyouId);
	}

	/**
	 * 伝票区分を採番し、ユーザー定義届書メタテーブルに登録する
	 * @param denpyouKbn 伝票区分
	 * @param userId ユーザーID
	 * @return 更新件数
	 */
	public int insertKaniTodokeMetaData(String denpyouKbn, String userId) {

		final String insSql = "INSERT INTO kani_todoke_meta VALUES (?, ?, current_timestamp, ?, current_timestamp) ";
		return connection.update(insSql, denpyouKbn, userId, userId);
	}

	/**
	 * バージョンを採番し、ユーザー定義届書バージョンテーブルに登録する
	 * @param denpyouKbn 伝票区分
	 * @param version バージョン
	 * @param denpyouName 伝票名
	 * @param denpyouNaiyou 伝票内容
	 * @param userId ユーザーID
	 * @return バージョン
	 */
	public int insertKaniTodokeVersion(String denpyouKbn, int version, String denpyouName, String denpyouNaiyou, String userId) {

		final String insSql = "INSERT INTO kani_todoke_version VALUES(?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)";
		return connection.update(insSql, denpyouKbn, version, denpyouName, denpyouNaiyou, userId, userId);
	}

	/**
	 * ユーザー定義届書項目定義を登録する
	 * @param denpyouKbn 伝票区分
	 * @param version バージョン
	 * @param gamenInfo 画面項目情報
	 * @param userId ユーザーID
	 * @return 登録件数
	 */
	public int insertKaniTodokeKoumokuTeigi(String denpyouKbn, int version, KaniTodokeGamenInfo gamenInfo, String userId) {

		int insCnt = 0;
		
		String[][] layoutArray2D = gamenInfo.getLayout();
		String[][] optionArray2D = gamenInfo.getOption();
		Map<Integer, Map<String, String>> mapValue1 = gamenInfo.getValue1();
		Map<Integer, Map<String, String>> mapValue2 = gamenInfo.getValue2();

		// 項目名とレイアウトを紐づけるためにマッピングする
		Map<String, String[]> mapItemLayoutMap = this.mapItemLayout(layoutArray2D);
		
		for (Integer denpyou_edano : mapValue1.keySet()) {

			Map<String, String> mapValue = mapValue1.get(denpyou_edano);

			for (String item_name : mapValue.keySet()) {

				String[] layout = mapItemLayoutMap.get(item_name);
				
				String area_kbn = layout[DataListIndex.AREA_KBN];                                        // エリア区分
				int hyouji_jun = Integer.parseInt(layout[DataListIndex.HYOUJI_JUN]);                     // 表示順
				String hissu_flg = layout[DataListIndex.HISSU_FLG];                                      // 必須フラグ
				String max_length = layout[DataListIndex.MAX_LENGTH];                                    // 最大桁数
				String min_value = layout[DataListIndex.MIN_VALUE];                                      // 最小値
				String max_value = layout[DataListIndex.MAX_VALUE];                                      // 最大値
				String label_name = layout[DataListIndex.LABEL_NAME];                                    // ラベル名
				String buhin_type = layout[DataListIndex.BUHIN_TYPE];                                    // 部品タイプ
				String buhin_format = layout[DataListIndex.BUHIN_FORMAT];                                // 部品形式
				String buhin_width = layout[DataListIndex.BUHIN_WIDTH];                                  // 部品幅
				String buhin_height = layout[DataListIndex.BUHIN_HEIGHT];                                // 部品高さ
				String checkbox_label_name = layout[DataListIndex.CHECKBOX_LABEL_NAME];                  // チェックボックスラベル名
				String master_kbn = layout[DataListIndex.MASTER_KBN];                                    // マスター区分
				String value1 = mapValue1.get(denpyou_edano).get(item_name) == null ? "" : mapValue1.get(denpyou_edano).get(item_name);      // 値１
				String value2 = mapValue2.get(denpyou_edano).get(item_name) == null ? "" : mapValue2.get(denpyou_edano).get(item_name);      // 値２
				String yosan_id = layout[DataListIndex.VALUE1];                                          // 予算執行項目ID
				String decimal_point = layout[DataListIndex.DECIMAL_POINT];                              // 小数点以下桁数
				String kotei_hyouji = layout[DataListIndex.KOTEI_HYOUJI];                                // 固定表示

				// ユーザー定義届書項目定義登録
				insCnt = insCnt + connection.update("INSERT INTO kani_todoke_koumoku VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)", 
						denpyouKbn, 
						version, 
						area_kbn, 
						item_name,
						hyouji_jun, 
						buhin_type,
						value1,
						value2,
						yosan_id,
						userId, 
						userId); 

				switch (buhin_type){
				case BuhinType.TEXT:

					// ユーザー定義届書項目テキスト
					connection.update("INSERT INTO kani_todoke_text VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)", 
							denpyouKbn, 
							version, 
							area_kbn, 
							item_name,
							label_name, 
							hissu_flg, 
							buhin_format,
							buhin_width, 
							Integer.parseInt(max_length), 
							decimal_point, 
							kotei_hyouji, 
							Long.parseLong(min_value), 
							Long.parseLong(max_value), 
							userId, 
							userId);

					break;

				case BuhinType.TEXTAREA:

					// ユーザー定義届書項目テキストエリア
					connection.update("INSERT INTO kani_todoke_textarea VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)", 
							denpyouKbn, 
							version, 
							area_kbn, 
							item_name,
							label_name, 
							hissu_flg, 
							buhin_width, 
							buhin_height, 
							Integer.parseInt(max_length), 
							kotei_hyouji, 
							userId, 
							userId);

					break;

				case BuhinType.CHECKBOX:

					// ユーザー定義届書項目チェックボックス
					connection.update("INSERT INTO kani_todoke_checkbox VALUES (?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)", 
							denpyouKbn, 
							version, 
							area_kbn, 
							item_name,
							label_name, 
							hissu_flg, 
							checkbox_label_name, 
							userId, 
							userId);

					break;

				case BuhinType.MASTER:

					// ユーザー定義届書項目マスター
					connection.update("INSERT INTO kani_todoke_master VALUES (?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)", 
							denpyouKbn, 
							version, 
							area_kbn, 
							item_name,
							label_name, 
							hissu_flg, 
							master_kbn, 
							userId, 
							userId);

					break;

				default:

					if(BuhinType.RADIO.equals(buhin_type) || BuhinType.PULLDOWN.equals(buhin_type))
					{
						// ユーザー定義届書項目リスト親(ラジオボタン／プルダウン)
						connection.update("INSERT INTO kani_todoke_list_oya VALUES (?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)", 
								denpyouKbn, 
								version, 
								area_kbn, 
								item_name,
								label_name, 
								hissu_flg, 
								userId, 
								userId);
					}

					break;
				}
			}
		}
		
		if (optionArray2D != null) {
			
			for (int i = 0; i < optionArray2D.length; i++) {

				String item_name = optionArray2D[i][OptionListIndex.ITEM_NAME]; // 項目名
				String area_kbn = optionArray2D[i][OptionListIndex.AREA_KBN]; // エリア区分
				int hyouji_jun = Integer.parseInt(optionArray2D[i][OptionListIndex.HYOUJI_JUN]); // 表示順
				String text = optionArray2D[i][OptionListIndex.OPTION_TEXT]; // オプションテキスト
				String value = optionArray2D[i][OptionListIndex.OPTION_VAL]; // オプション値
				String select_item = optionArray2D[i][OptionListIndex.SELECT_ITEM]; // 選択項目名

				// ユーザー定義届書項目リスト子
				connection.update("INSERT INTO kani_todoke_list_ko VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)", 
						denpyouKbn, 
						version, 
						area_kbn, 
						item_name,
						hyouji_jun,
						text, 
						value, 
						select_item,
						userId, 
						userId);
			}
		}
		
		return insCnt;
	}

	/**
	 * 伝票種別一覧を登録する
	 * @param denpyouKbn 伝票区分
	 * @param denpyouName 伝票名
	 * @param denpyouNaiyou 伝票内容
	 * @param userId ユーザーID
	 * @return 更新件数
	 */
	public int insertDenpyouShubetsuIchiran(String denpyouKbn, String denpyouName, String denpyouNaiyou, String userId) {

		// 最大表示順
		GMap map = connection.find("SELECT COALESCE(MAX(hyouji_jun),0) + 1 AS hyouji_jun FROM denpyou_shubetsu_ichiran");

		final String sql = "INSERT INTO denpyou_shubetsu_ichiran ("
						 + "            denpyou_kbn"
						 + "           ,version"
						 + "           ,denpyou_shubetsu"
						 + "           ,hyouji_jun"
						 + "           ,gyoumu_shubetsu"
						 + "           ,naiyou"
						 + "           ,denpyou_shubetsu_url"
						 + "           ,yuukou_kigen_from"
						 + "           ,yuukou_kigen_to"
						 + "           ,kanren_sentaku_flg"
						 + "           ,kanren_hyouji_flg"
						 + "           ,denpyou_print_flg"
						 + "           ,shinsei_shori_kengen_name"
						 + "           ,touroku_user_id"
						 + "           ,touroku_time"
						 + "           ,koushin_user_id"
						 + "           ,koushin_time"
						 + "           ) VALUES ("
						 + "            ?"
						 + "           ,1"
						 + "           ,?"
						 + "           ,?"
						 + "           ,'ユーザー定義届書'"
						 + "           ,?"
						 + "           ,'kani_todoke'"
						 + "           ,current_date"
						 + "           ,TO_DATE('9999/12/31', 'YYYY/MM/DD')"
						 + "           ,'0'"
						 + "           ,'0'"
						 + "           ,'0'"
						 + "           ,''"
						 + "           ,?"
						 + "           ,current_timestamp"
						 + "           ,?"
						 + "           ,current_timestamp"
						 + "           )";

		return connection.update(sql, denpyouKbn, denpyouName, Integer.parseInt(map.get("hyouji_jun").toString()), denpyouNaiyou, userId, userId);
	}

	/**
	 * 伝票種別一覧を更新する
	 * @param denpyouKbn 伝票区分
	 * @param version バージョン
	 * @param denpyouName 伝票名
	 * @param denpyouNaiyou 伝票内容
	 * @param userId ユーザーID
	 * @return 更新件数
	 */
	public int updateDenpyouShubetsuIchiran(String denpyouKbn, int version, String denpyouName, String denpyouNaiyou, String userId) {

		final String sql = "UPDATE denpyou_shubetsu_ichiran SET denpyou_shubetsu = ?, naiyou = ?, version = ?, koushin_user_id = ?, koushin_time = current_timestamp WHERE denpyou_kbn = ?";
		return connection.update(sql, denpyouName, denpyouNaiyou, version, userId, denpyouKbn);
	}
	
	/**
	 * ユーザー定義届書内容を更新する
	 * @param denpyouKbn 伝票区分
	 * @param denpyouNaiyou 伝票内容
	 * @param userId ユーザーID
	 * @return 更新件数
	 */
	public int updateKaniTodokeNaiyou(String denpyouKbn, String denpyouNaiyou, String userId) {

		int cnt = 0;
		
		GMap map = connection.find("select version, naiyou from denpyou_shubetsu_ichiran where denpyou_kbn = ?", denpyouKbn);
		
		Integer version = Integer.parseInt(map.get("version").toString());
		String naiyou = map.get("naiyou").toString();
		
		if (version > 0) {
			final String sql = "UPDATE kani_todoke_version SET naiyou = ?, koushin_user_id = ?, koushin_time = current_timestamp WHERE denpyou_kbn = ? AND version = ? AND naiyou != ?";
			cnt = connection.update(sql, denpyouNaiyou, userId, denpyouKbn, version, naiyou);
		}
		
		return cnt;
	}
	
	/** ユーザー定義届書を削除
	 * @param denpyouKbn 伝票区分
	 */
	public void deleteKaniTodoke(String denpyouKbn) {

		connection.update("DELETE FROM kani_todoke_meta WHERE denpyou_kbn = ?", denpyouKbn);
		connection.update("DELETE FROM kani_todoke_version WHERE denpyou_kbn = ?", denpyouKbn);
		connection.update("DELETE FROM kani_todoke_koumoku WHERE denpyou_kbn = ?", denpyouKbn);
		connection.update("DELETE FROM kani_todoke_text WHERE denpyou_kbn = ?", denpyouKbn);
		connection.update("DELETE FROM kani_todoke_textarea WHERE denpyou_kbn = ?", denpyouKbn);
		connection.update("DELETE FROM kani_todoke_checkbox WHERE denpyou_kbn = ?", denpyouKbn);
		connection.update("DELETE FROM kani_todoke_master WHERE denpyou_kbn = ?", denpyouKbn);
		connection.update("DELETE FROM kani_todoke_list_oya WHERE denpyou_kbn = ?", denpyouKbn);
		connection.update("DELETE FROM kani_todoke_list_ko WHERE denpyou_kbn = ?", denpyouKbn);
		connection.update("DELETE FROM denpyou_shubetsu_ichiran WHERE denpyou_kbn = ?", denpyouKbn);
	}

	/**
	 * 画面の要素を復元するためのHTMLを生成する
	 * @param mode HTML作成モード
	 * @param area_kbn エリア区分
	 * @return HTML
	 */
	public String createHtml(int mode, String area_kbn) {
		return createHtml(mode, area_kbn, new KaniTodokeGamenInfo());
	}

	/**
	 * 画面の要素を復元するためのHTMLを生成する
	 * @param mode HTML作成モード
	 * @param area_kbn エリア区分
	 * @param gamenInfo 画面項目情報
	 * @return HTML
	 */
	public String createHtml(int mode, String area_kbn, KaniTodokeGamenInfo gamenInfo) {
		return createHtml(mode, area_kbn, gamenInfo, true);
	}

	/**
	 * 画面の要素を復元するためのHTMLを生成する
	 * @param mode HTML作成モード
	 * @param area_kbn エリア区分
	 * @param gamenInfo 画面項目情報
	 * @param enableInput 入力制御
	 * @return HTML
	 */
	public String createHtml(int mode, String area_kbn, KaniTodokeGamenInfo gamenInfo, Boolean enableInput) {

		String rplContents = "{contents}";
		String htmlMain = "";
		StringBuilder htmlContents = new StringBuilder();

		if(mode == HtmlCreateMode.WORKFLOW) {

			if (area_kbn.equals(AREA_KBN.SHINSEI)) {
				htmlMain += "<section print-unit>";
			}
			if (area_kbn.equals(AREA_KBN.MEISAI)) {
				htmlMain += "<section id='meisaiList'>";
			}
			htmlMain += rplContents;
			htmlMain += "</section>";

		} else {
			htmlMain += "<section id='" + area_kbn + "Section'>";
			htmlMain += rplContents;
			htmlMain += "</section>";
		}

		String[][] layoutArray2D = gamenInfo.getLayout();
		String[][] optionArray2D = gamenInfo.getOption();
		Map<Integer, Map<String, String>> mapValue1 = gamenInfo.getValue1();
		Map<Integer, Map<String, String>> mapValue2 = gamenInfo.getValue2();

		if (layoutArray2D.length == 0) {
			if (mode == HtmlCreateMode.LAYOUT) {
				switch (area_kbn){
				case AREA_KBN.SHINSEI:
					htmlContents.append("<h2>申請内容</h2>");
					htmlContents.append("<div id='shinseiList'>");
					htmlContents.append("</div>");
					break;

				case AREA_KBN.MEISAI:
					htmlContents.append("<h2>明細</h2>");
					htmlContents.append("<div id='meisaiList'>");
					htmlContents.append("</div>");
					break;
				}
			}

		} else {
			// 項目名とレイアウトを紐づけるためにマッピングする
			Map<String, String[]> mapItemLayoutMap = this.mapItemLayout(layoutArray2D);

			// 項目名とオプションを紐づけるためにマッピングする
			Map<String, List<String[]>> mapOption = this.mapItemOption(optionArray2D);

			for (Integer denpyou_edano : gamenInfo.getSortDenpyouEdano(mapValue1)) {
				String htmlContentsDetail = "";
				Map<String, String> mapValue = mapValue1.get(denpyou_edano);
				for (String item_name : gamenInfo.getItemSortList(mapValue)) {
					String[] layout = mapItemLayoutMap.get(item_name);
					String buhin_type = layout[DataListIndex.BUHIN_TYPE]; // 部品タイプ
					String label_name = layout[DataListIndex.LABEL_NAME];     						// ラベル名
					String hissu_flg = layout[DataListIndex.HISSU_FLG];       						// 必須フラグ
					String max_length = layout[DataListIndex.MAX_LENGTH];     						// 最大桁数
					String min_value = layout[DataListIndex.MIN_VALUE];     						// 最小値
					String max_value = layout[DataListIndex.MAX_VALUE];     						// 最大値
					String buhin_format = layout[DataListIndex.BUHIN_FORMAT]; // 部品形式
					String buhin_width = layout[DataListIndex.BUHIN_WIDTH];   						// 部品幅
					String buhin_height = layout[DataListIndex.BUHIN_HEIGHT]; // 部品高さ
					String checkbox_label_name = layout[DataListIndex.CHECKBOX_LABEL_NAME]; // チェックボックスラベル名
					String master_kbn = layout[DataListIndex.MASTER_KBN]; // マスター区分
					String yosan_id = layout[DataListIndex.VALUE1]; // 予算執行項目ID
					String decimal_point = layout[DataListIndex.DECIMAL_POINT]; // 小数点以下桁数
					String kotei_hyouji = layout[DataListIndex.KOTEI_HYOUJI]; // 固定表示

					String value1 = mapValue1.get(denpyou_edano).get(item_name);
					String value2 = mapValue2.get(denpyou_edano).get(item_name);

					String value1_name = item_name + "_val1"; // 値名
					String value2_name = item_name + "_val2"; // 値名
					String hissu_span = (hissu_flg.equals("1")) ? "<span class='required'>*</span>" : ""; // 必須フラグ
					String select_button = item_name + "_btn"; // 選択ボタン
					String clear_button = item_name + "_clrbtn"; // クリアボタン
					String inputItem = "";
					String value_checked = "";
					StringBuffer sbOption = new StringBuffer();

					switch (buhin_type){
					case BuhinType.TEXT:
						if (kotei_hyouji.equals(EteamNaibuCodeSetting.KOTEI_HYOUJI.YES) && mode == HtmlCreateMode.WORKFLOW) {
							inputItem += "<p style='margin-top:5px'>{value}</p>";
							inputItem = inputItem.replace("{value}",EteamFormatter.htmlEscape(String.valueOf(value1))); 
						} else {
							inputItem += "<input type='hidden' name='min_value' value='{min_value}'>";
							inputItem += "<input type='hidden' name='max_value' value='{max_value}'>";
							inputItem += "<input type='text' name='{value_name}' class='input-inline {buhin_width} {buhin_format}' maxlength='{max_length}' {list} value='{value}' yosanId='{yosanId}' decimalPoint='{decimal_point}' koteiHyouji='{kotei_hyouji}'>{yen}";
							inputItem = inputItem.replace("{decimal_point}",EteamFormatter.htmlEscape(String.valueOf(decimal_point))); // 小数点以下桁数
							inputItem = inputItem.replace("{kotei_hyouji}",EteamFormatter.htmlEscape(String.valueOf(kotei_hyouji))); // 固定表示

							// 部品形式
							if (buhin_format.equals(BUHIN_FORMAT.MONEY) && !enableInput) {
								inputItem = inputItem.replace("{buhin_format}","autoNumeric");
							} else {
								inputItem = inputItem.replace("{buhin_format}",buhin_format);
							}

							if(buhin_format.equals(BUHIN_FORMAT.DATE) || buhin_format.equals(BUHIN_FORMAT.TIME)){
								//Androidでの文字入力のため、日付・時刻の場合は最大桁数削除
								inputItem = inputItem.replace("maxlength='{max_length}'",""); // 最大桁数
							}else{
								inputItem = inputItem.replace("{max_length}",max_length); // 最大桁数
							}
							
							if(buhin_format.equals(BUHIN_FORMAT.TIME)){
								inputItem = inputItem.replace("{list}",""); // 時刻リスト
							}else{
								inputItem = inputItem.replace("{list}",""); // リストなし
							}
							
							inputItem = inputItem.replace("{min_value}",min_value); // 最小値
							inputItem = inputItem.replace("{max_value}",max_value); // 最大値
							inputItem = inputItem.replace("{buhin_width}",buhin_width); // 部品幅
							inputItem = buhin_format.equals(BUHIN_FORMAT.MONEY) ? inputItem.replace("{yen}","円") : inputItem.replace("{yen}",""); // 円マーク
							inputItem = inputItem.replace("{yosanId}",yosan_id); // 予算執行項目ID
							inputItem = inputItem.replace("{value_name}",value1_name);     											// 値名
							inputItem = inputItem.replace("{value}",EteamFormatter.htmlEscape(String.valueOf(value1))); // 値
						}

						break;

					case BuhinType.TEXTAREA:
						if (kotei_hyouji.equals(EteamNaibuCodeSetting.KOTEI_HYOUJI.YES) && mode == HtmlCreateMode.WORKFLOW) {
							inputItem = "<p style='margin-top:5px'>{value}</p>";
							inputItem = inputItem.replace("{value}",EteamFormatter.htmlEscapeBr(String.valueOf(value1)));
						} else {
							inputItem = "<textarea name='{value_name}' class='input-inline {buhin_width} {buhin_height}' maxlength='{max_length}' yosanId='{yosanId}' koteiHyouji='{kotei_hyouji}'>{value}</textarea>";
							inputItem = inputItem.replace("{max_length}",max_length); // 最大桁数
							inputItem = inputItem.replace("{buhin_width}",buhin_width); // 部品幅
							inputItem = inputItem.replace("{buhin_height}",buhin_height); // 部品高さ
							inputItem = inputItem.replace("{yosanId}",yosan_id); // 予算執行項目ID
							inputItem = inputItem.replace("{kotei_hyouji}",EteamFormatter.htmlEscape(String.valueOf(kotei_hyouji)));// 固定表示
							inputItem = inputItem.replace("{value_name}",value1_name); // 値名
							inputItem = inputItem.replace("{value}",EteamFormatter.htmlEscape(String.valueOf(value1))); // 値
						}
						
						break;

					case BuhinType.CHECKBOX:

						if (value1.equals("1")) {
							value_checked = "checked";
						} else {
							value_checked = ""; 
						}

						inputItem += "<input type='hidden' name='{value_name}' value='0'>"; 
						inputItem += "<label class='checkbox inline'><input type='checkbox' name='{value_name}_{denpyou_edano}' value='1' {checked}>{checkbox_label_name}</label>"; 
						
						inputItem = inputItem.replace("{value_name}",value1_name); // 値名
						inputItem = inputItem.replace("{value}",value1); // 値
						inputItem = inputItem.replace("{checked}",value_checked); // チェック
						inputItem = inputItem.replace("{checkbox_label_name}",checkbox_label_name); // チェックボックスラベル名
						inputItem = inputItem.replace("{denpyou_edano}",denpyou_edano.toString()); // 伝票枝番号
						
						break;

					case BuhinType.RADIO:

						for (String[] optionArray : mapOption.get(item_name)) {

							String option_text = optionArray[OptionListIndex.OPTION_TEXT];
							String option_value = optionArray[OptionListIndex.OPTION_VAL];
							String select_item = optionArray[OptionListIndex.SELECT_ITEM];

							String option = "<label class='radio inline'><input type='radio' name='{value_name}_{denpyou_edano}' data-item='{select_item}' data-text='{option_text}' value='{option_value}' {checked}>{option_text}</label>";
							option = option.replace("{value_name}",value1_name); // 値名
							option = option.replace("{denpyou_edano}",denpyou_edano.toString()); // 伝票枝番号
							option = option.replace("{select_item}",EteamFormatter.htmlEscape(String.valueOf(select_item))); // 選択項目名
							option = option.replace("{option_text}",EteamFormatter.htmlEscape(String.valueOf(option_text))); // 値名
							option = option.replace("{option_value}",EteamFormatter.htmlEscape(String.valueOf(option_value))); // 値名

							if (value1.equals(option_value)) {
								option = option.replace("{checked}","checked");
							} else {
								option = option.replace("{checked}","");
							}

							sbOption.append(option);
						}

						inputItem = "<input type='hidden' name='{value_name}' value='{value}'>{option}";
						inputItem = inputItem.replace("{value_name}",value1_name); // 値名
						inputItem = inputItem.replace("{value}",EteamFormatter.htmlEscape(String.valueOf(value1))); // 値
						inputItem = inputItem.replace("{option}",sbOption.toString()); // オプション

						break;

					case BuhinType.PULLDOWN:

						for (String[] optionArray : mapOption.get(item_name)) {

							String option_text = optionArray[OptionListIndex.OPTION_TEXT];
							String option_value = optionArray[OptionListIndex.OPTION_VAL];
							String select_item = optionArray[OptionListIndex.SELECT_ITEM];

							String option = "<option data-item='{select_item}' value='{option_value}' {selected}>{option_text}</option>";
							option = option.replace("{select_item}",EteamFormatter.htmlEscape(String.valueOf(select_item))); // 選択項目名
							option = option.replace("{option_text}",EteamFormatter.htmlEscape(String.valueOf(option_text))); // 値名
							option = option.replace("{option_value}",EteamFormatter.htmlEscape(String.valueOf(option_value))); // 値名

							if (value1.equals(option_value)) {
								option = option.replace("{selected}","selected");
							} else {
								option = option.replace("{selected}","");
							}

							sbOption.append(option);
						}

						inputItem = "<select name='{value_name}' class='input-large'>{option}</select>";
						inputItem = inputItem.replace("{value_name}",value1_name); // 値名
						inputItem = inputItem.replace("{option}",sbOption.toString()); // オプション

						break;

					case BuhinType.MASTER:

						switch (master_kbn){
						case MasterKbn.KAMOKU:
						case MasterKbn.BUMON:
							inputItem += "<input type='hidden' name='master_kbn' value='{master_kbn}' yosanId='{yosanId}'>";
							inputItem += "<input type='text' name='{value1_name}' class='input-small pc_only' value='{value1}' onblur='{master_kbn}LostFocus(this)' onchange='{master_kbn}Change(this)'> ";
							inputItem += "<input type='text' name='{value2_name}' class='input-xlarge' value='{value2}' disabled> ";
							inputItem += "<button type='button' name='{select_button}' class='btn btn-small' onclick='{master_kbn}(this)'>選択</button> ";
							inputItem += "<button type='button' name='{clear_button}' class='btn btn-small' onclick='clearMaster(this)'>クリア</button>";
							inputItem = inputItem.replace("{master_kbn}",master_kbn); // マスター区分
							inputItem = inputItem.replace("{select_button}",select_button); // ボタン名
							inputItem = inputItem.replace("{clear_button}",clear_button); // ボタン名
							inputItem = inputItem.replace("{value1_name}",value1_name); // 値名１
							inputItem = inputItem.replace("{value2_name}",value2_name); // 値名２
							inputItem = inputItem.replace("{value1}",EteamFormatter.htmlEscape(String.valueOf(value1))); // 値
							inputItem = inputItem.replace("{value2}",EteamFormatter.htmlEscape(String.valueOf(value2))); // 値
							inputItem = inputItem.replace("{yosanId}",yosan_id); // 予算執行項目ID
							break;

						case "kanjyouKamokuEdabanSentaku":
						case MasterKbn.TORIHIKISAKI:	
							inputItem += "<input type='hidden' name='master_kbn' value='{master_kbn}' yosanId='{yosanId}'>";
							inputItem += "<input type='text' name='{value1_name}' class='input-medium pc_only' value='{value1}' onblur='{master_kbn}LostFocus(this)'> ";
							inputItem += "<input type='text' name='{value2_name}' class='input-xlarge' value='{value2}' disabled> ";
							inputItem += "<button type='button' name='{select_button}' class='btn btn-small' onclick='{master_kbn}(this)'>選択</button> ";
							inputItem += "<button type='button' name='{clear_button}' class='btn btn-small' onclick='clearMaster(this)'>クリア</button>";
							inputItem = inputItem.replace("{master_kbn}",master_kbn); // マスター区分
							inputItem = inputItem.replace("{select_button}",select_button); // ボタン名
							inputItem = inputItem.replace("{clear_button}",clear_button); // ボタン名
							inputItem = inputItem.replace("{value1_name}",value1_name); // 値名１
							inputItem = inputItem.replace("{value2_name}",value2_name); // 値名２
							inputItem = inputItem.replace("{value1}",EteamFormatter.htmlEscape(String.valueOf(value1))); // 値
							inputItem = inputItem.replace("{value2}",EteamFormatter.htmlEscape(String.valueOf(value2))); // 値
							inputItem = inputItem.replace("{yosanId}",yosan_id); // 予算執行項目ID
							break;

						case MasterKbn.UF1:	
						case MasterKbn.UF2:	
						case MasterKbn.UF3:	
							inputItem += "<input type='hidden' name='master_kbn' value='{master_kbn}' yosanId='{yosanId}'>";
							inputItem += "<input type='text' name='{value1_name}' class='input-large pc_only' value='{value1}' onblur='{master_kbn}LostFocus(this)'> ";
							inputItem += "<input type='text' name='{value2_name}' class='input-xlarge' value='{value2}' disabled> ";
							inputItem += "<button type='button' name='{select_button}' class='btn btn-small' onclick='{master_kbn}(this)'>選択</button> ";
							inputItem += "<button type='button' name='{clear_button}' class='btn btn-small' onclick='clearMaster(this)'>クリア</button>";
							inputItem = inputItem.replace("{master_kbn}",master_kbn); // マスター区分
							inputItem = inputItem.replace("{select_button}",select_button); // ボタン名
							inputItem = inputItem.replace("{clear_button}",clear_button); // ボタン名
							inputItem = inputItem.replace("{value1_name}",value1_name); // 値名１
							inputItem = inputItem.replace("{value2_name}",value2_name); // 値名２
							inputItem = inputItem.replace("{value1}",EteamFormatter.htmlEscape(String.valueOf(value1))); // 値
							inputItem = inputItem.replace("{value2}",EteamFormatter.htmlEscape(String.valueOf(value2))); // 値
							inputItem = inputItem.replace("{yosanId}",yosan_id); // 予算執行項目ID
							break;
						}
						break;
					}

					// HTML 
					StringBuilder sbHtml = new StringBuilder();

					switch(mode){
					case HtmlCreateMode.LAYOUT:
						sbHtml.append("<div class='control-group " + area_kbn + " ui-state-default'>");
						sbHtml.append("<label class='control-label'>{hissu_flg}{label_name}</label>");
						sbHtml.append("<input type='hidden' name='item_name'  value='{item_name}'>");
						sbHtml.append("<input type='hidden' name='buhin_type' value='{buhin_type}'>");
						sbHtml.append("    <div class='controls'>");
						sbHtml.append("        {input_item}");
						sbHtml.append("        <button type='button' name='rowChange' class='btn btn-mini' style='margin-left:3px'>変更</button>");
						sbHtml.append("        <button type='button' name='rowDelete' class='btn btn-mini' style='margin-left:3px'>削除</button>");
						sbHtml.append("        <button type='button' name='rowUp'     class='btn btn-mini' style='margin-left:3px'>↑</button>");
						sbHtml.append("        <button type='button' name='rowDown'   class='btn btn-mini' style='margin-left:3px'>↓</button>");
						sbHtml.append("    </div>");
						sbHtml.append("</div>");
						break;
					case HtmlCreateMode.WORKFLOW:
						sbHtml.append("<div class='control-group'>");
						sbHtml.append("<label class='control-label'>{hissu_flg}{label_name}</label>");
						sbHtml.append("<input type='hidden' name='item_name'  value='{item_name}'>");
						sbHtml.append("    <div class='controls'>");
						sbHtml.append("        {input_item}");
						sbHtml.append("    </div>");
						sbHtml.append("</div>");
						break;
					}

					String html = sbHtml.toString();

					html = html.replace("{item_name}",item_name); // 項目名
					html = html.replace("{buhin_type}",buhin_type); // 部品タイプ
					html = html.replace("{hissu_flg}",hissu_span); // 必須フラグ
					html = html.replace("{label_name}",label_name); // ラベル名
					html = html.replace("{input_item}",inputItem); // 入力項目

					htmlContentsDetail += html;
				}

				switch (area_kbn){
				case AREA_KBN.SHINSEI:

					htmlContents.append("<h2>申請内容</h2>");
					htmlContents.append("<div id='shinseiList'>");
					htmlContents.append(htmlContentsDetail);
					htmlContents.append("</div>");

					break;

				case AREA_KBN.MEISAI:

					switch(mode){

					case HtmlCreateMode.LAYOUT:
						htmlContents.append("<h2>明細</h2>");
						htmlContents.append("<div id='meisaiList'>");
						htmlContents.append(htmlContentsDetail);
						htmlContents.append("</div>");
						break;
						
					case HtmlCreateMode.WORKFLOW:
						htmlContents.append("<div class='blank meisai print-unit'>");
						htmlContents.append("<h2>");
						htmlContents.append("明細#<span class='number'>" + denpyou_edano + "</span> ");
						htmlContents.append("<span class='non-print'>");
						htmlContents.append("<button type='button' name='meisaiAdd' class='btn '>追加</button> ");
						htmlContents.append("<button type='button' name='meisaiDelete' class='btn '>削除</button> ");
						htmlContents.append("<button type='button' name='meisaiUp' class='btn '>↑</button> ");
						htmlContents.append("<button type='button' name='meisaiDown' class='btn '>↓</button> ");
						htmlContents.append("<button type='button' name='meisaiCopy' class='btn '>ｺﾋﾟｰ</button>");
						htmlContents.append("</span>");
						htmlContents.append("</h2>");
						htmlContents.append("<div>");
						htmlContents.append(htmlContentsDetail);
						htmlContents.append("</div>");
						htmlContents.append("</div>");
						break;
					}
					break;
				}
			}
		}

		htmlMain = htmlMain.replace(rplContents, htmlContents.toString());

		return htmlMain;
	}
	
	
	/**
	 * 伝票枝番号とデータを紐づけるためにマッピングする
	 * @param dataList データリスト
	 * @return マップ情報
	 */
	public Map<Integer, List<String[]>> mapDenpyouEdanoData(String[][] dataList) {
		
		Map<Integer, List<String[]>> mapDenpyouEdano = new HashMap<Integer, List<String[]>>();
		Integer denpyou_edanoTmp = 0;
		
		if (dataList != null) {

			for (String[] data: dataList) {

				Integer denpyou_edano = Integer.parseInt(data[DataListIndex.DENPYOU_EDANO]);

				if(!denpyou_edano.equals(denpyou_edanoTmp)) {
					denpyou_edanoTmp = denpyou_edano;
					mapDenpyouEdano.put(denpyou_edano, new ArrayList<String[]>());
				}

				List<String[]> listTmp = mapDenpyouEdano.get(denpyou_edano);
				listTmp.add(data);
				mapDenpyouEdano.put(denpyou_edano, listTmp);
			}
		}
		
		return mapDenpyouEdano;
	}
	
	/**
	 * 項目名とレイアウトを紐づけるためにマッピングする
	 * @param layoutArray レイアウト
	 * @return マップ情報
	 */
	public Map<String, String[]> mapItemLayout(String[][] layoutArray) {
		
		Map<String, String[]> mapData = new HashMap<String, String[]>();
		
		if (layoutArray != null) {

			for (String[] layout: layoutArray) {

				String item_name = layout[DataListIndex.ITEM_NAME];
				mapData.put(item_name, layout);
			}
		}
		
		return mapData;
	}
	
	/**
	 * 項目名とオプションを紐づけるためにマッピングする
	 * @param optionList オプションリスト
	 * @return マップ情報
	 */
	public Map<String, List<String[]>> mapItemOption(String[][] optionList) {
		
		Map<String, List<String[]>> mapOption = new HashMap<String, List<String[]>>();
		
		if (optionList != null) {

			for (String[] option: optionList) {

				String item_name = option[DataListIndex.ITEM_NAME];

				if(!mapOption.containsKey(item_name)) {
					mapOption.put(item_name, new ArrayList<String[]>());
				}

				List<String[]> optionListTmp = mapOption.get(item_name);
				optionListTmp.add(option);
				mapOption.put(item_name, optionListTmp);
			}
		}
		
		return mapOption;
	}
	
	/**
	 * 項目名からオプション値を取得する
	 * @param item_name 項目名
	 * @param optionList オプションリスト
	 * @return オプション値
	 */
	public String[] getItemOption(String item_name, String[][] optionList) {
		
		Map<String, List<String[]>> mapOption = this.mapItemOption(optionList);
		List<String> listVal = new ArrayList<String>();
		
		for (String[] optionArray : mapOption.get(item_name)) {
			String value = optionArray[OptionListIndex.OPTION_VAL];
			listVal.add(value);
		}
		
		return listVal.toArray(new String[0]);
	}
	
	/**
	 * 選択項目マスタに該当データの選択項目存在するかチェック
	 * @param item_name 項目名
	 * @param optionList オプションリスト
	 * @return マスタ存在するならtrue
	 */
	public boolean checkSelectItemExist(String item_name, String[][] optionList) {
		//現状名前チェックのみ
		
		Map<String, List<String[]>> mapOption = this.mapItemOption(optionList);
		String value = "";
		for (String[] optionArray : mapOption.get(item_name)) {
			value = optionArray[OptionListIndex.SELECT_ITEM];
			if (!value.isEmpty())
			{
				break;
			}
		}
		if (value.isEmpty())
		{
			return false;
		} //最後までvalue取得できなければエラー
		
		String sql = "SELECT * FROM kani_todoke_select_item WHERE select_item = ?";
		List<GMap> ret = connection.load(sql, value);
		return (ret != null && ret.size() > 0) ? true : false;
	}

	/**
	 * 予算執行項目入力部品件数取得<br>
	 * ユーザー定義届書レイアウトに指定された予算執行項目の入力部品件数を取得する。<br>
	 * 
	 * @param denpyouKbn 伝票区分
	 * @param version バーション
	 * @param yosanShikkouKoumokuId 予算執行項目ID
	 * @return 件数
	 */
	public int chkExistInputParts(String denpyouKbn, String version, String yosanShikkouKoumokuId){

		//  ユーザー定義届書内のSelect文を集約したLogic
		KaniTodokeCategoryLogic kaniTodokeLogic = EteamContainer.getComponent(KaniTodokeCategoryLogic.class, super.connection);

		/*
		 * ユーザー定義届書レイアウトから入力部品の件数を取得する。
		 */
		int cntParts = kaniTodokeLogic.findYosanShikkouKoumokuCnt(denpyouKbn, version, yosanShikkouKoumokuId);
		return cntParts;
	}

	/**
	 * 相関チェック<br>
	 * 登録ボタン押下時に以下の相関チェックを実施する。<br>
	 * <ul>
	 * <li>科目枝番相関チェック</li>
	 * <li>合計金額相関チェック</li>
	 * <li>収支差額相関チェック</li>
	 * <li>ルート判定金額項目削除チェック</li>
	 * </ul>
	 * 
	 * @param denpyouKbn 伝票区分
	 * @param shinseiGamenInfo 画面情報（申請内容）
	 * @param meisaiGamenInfo 画面情報（明細）
	 * @return エラーメッセージリスト
	 */
	public List<String> soukanCheck(String denpyouKbn, KaniTodokeGamenInfo shinseiGamenInfo, KaniTodokeGamenInfo meisaiGamenInfo) {

		List<String> errorList = new ArrayList<String>();
		String[][] shinseiLayout = shinseiGamenInfo.getLayout();
		String[][] meisaiLayout = meisaiGamenInfo.getLayout();

		/*
		 * 科目枝番相関チェック
		 */

		// 収入枝番の入力部品がレイアウトに存在するか確認する。
		String preString = "収入";
		boolean isExistEdaNum = false;
		boolean isExistKamoku = false;
		for (int i = 0; i < meisaiLayout.length; i++){
			if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHUUNYUU_EDA.equals(meisaiLayout[i][DataListIndex.VALUE1])) {
				// 枝番が存在する
				isExistEdaNum = true;
				break;
			}
		}
		if (isExistEdaNum){
			// 存在する場合、科目の存在を確認する。
			for (int i = 0; i < meisaiLayout.length; i++){
				if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHUUNYUU_KAMOKU.equals(meisaiLayout[i][DataListIndex.VALUE1])) {
					// 科目が存在する
					isExistKamoku = true;
					break;
				}
			}
		}
		// 結果判定
		if (isExistEdaNum && !isExistKamoku){
			// 枝番が存在して科目が存在しない場合はエラーとする。
			errorList.add(preString + "枝番を使用する場合は" + preString + "科目も追加して下さい。");
		}

		// 支出枝番の入力部品がレイアウトに存在するか確認する。
		preString = "支出";
		isExistEdaNum = false;
		isExistKamoku = false;
		for (int i = 0; i < meisaiLayout.length; i++){
			if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_EDA.equals(meisaiLayout[i][DataListIndex.VALUE1])) {
				// 枝番が存在する
				isExistEdaNum = true;
				break;
			}
		}
		if (isExistEdaNum){
			// 存在する場合、科目の存在を確認する。
			for (int i = 0; i < meisaiLayout.length; i++){
				if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_KAMOKU.equals(meisaiLayout[i][DataListIndex.VALUE1])) {
					// 科目が存在する
					isExistKamoku = true;
					break;
				}
			}
		}
		// 結果判定
		if (isExistEdaNum && !isExistKamoku){
			// 枝番が存在して科目が存在しない場合はエラーとする。
			errorList.add(preString + "枝番を使用する場合は" + preString + "科目も追加して下さい。");
		}

		/*
		 * 合計金額相関チェック
		 */

		// 収入金額合計と収入金額の入力部品がレイアウトに存在するか確認する。
		preString = "収入";
		boolean isExistGoukei = false;
		boolean isExistKingaku = false;
		for (int i = 0; i < shinseiLayout.length; i++){
			if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHUUNYUU_GOUKEI.equals(shinseiLayout[i][DataListIndex.VALUE1])) {
				// 金額合計が存在する
				isExistGoukei = true;
				break;
			}
		}
		for (int i = 0; i < meisaiLayout.length; i++){
			if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHUUNYUU_KINGAKU.equals(meisaiLayout[i][DataListIndex.VALUE1])) {
				// 金額が存在する
				isExistKingaku = true;
				break;
			}
		}
		// 結果判定
		if (isExistGoukei && !isExistKingaku){
			// 合計が存在して金額が存在しない場合はエラーとする。
			errorList.add(preString + "金額合計を使用する場合は" + preString + "金額の入力部品を明細に追加して下さい。");
		}

		// 支出金額合計と支出金額の入力部品がレイアウトに存在するか確認する。
		preString = "支出";
		isExistGoukei = false;
		isExistKingaku = false;
		for (int i = 0; i < shinseiLayout.length; i++){
			if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_GOUKEI.equals(shinseiLayout[i][DataListIndex.VALUE1])) {
				// 金額合計が存在する
				isExistGoukei = true;
				break;
			}
		}
		for (int i = 0; i < meisaiLayout.length; i++){
			if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_KINGAKU.equals(meisaiLayout[i][DataListIndex.VALUE1])) {
				// 金額が存在する
				isExistKingaku = true;
				break;
			}
		}
		// 結果判定
		if (isExistGoukei && !isExistKingaku){
			// 合計が存在して金額が存在しない場合はエラーとする。
			errorList.add(preString + "金額合計を使用する場合は" + preString + "金額の入力部品を明細に追加して下さい。");
		}

		/*
		 * 収支差額相関チェック
		 */

		// 収支差額と収入金額合計と支出金額合計の入力部品がレイアウトに存在するか確認する。
		boolean isExistSagaku = false;
		boolean isExistShuuNyuu = false;
		boolean isExistShishutsu = false;
		for (int i = 0; i < shinseiLayout.length; i++){
			if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHUUSHI_SAGAKU.equals(shinseiLayout[i][DataListIndex.VALUE1])) {
				// 収支差額が存在する
				isExistSagaku = true;
				continue;
			}
			if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHUUNYUU_GOUKEI.equals(shinseiLayout[i][DataListIndex.VALUE1])) {
				// 収入金額合計が存在する
				isExistShuuNyuu = true;
				continue;
			}
			if (EteamNaibuCodeSetting.YOSAN_SHIKKOU_KOUMOKU_ID.SHISHUTSU_GOUKEI.equals(shinseiLayout[i][DataListIndex.VALUE1])) {
				// 支出金額合計が存在する
				isExistShishutsu = true;
			}
		}
		// 結果判定
		if (isExistSagaku && !isExistShuuNyuu && !isExistShishutsu){
			// 収支差額は存在するが、収入金額合計、支出金額合計が存在しない場合はエラーとする。
			errorList.add("収支差額を使用する場合は収入金額合計と支出金額合計を追加して下さい。");
		}
		if (isExistSagaku && isExistShuuNyuu && !isExistShishutsu){
			// 収支差額、収入金額合計は存在するが、支出金額合計が存在しない場合はエラーとする。
			errorList.add("収支差額を使用する場合は支出金額合計を追加して下さい。");
		}
		if (isExistSagaku && !isExistShuuNyuu && isExistShishutsu){
			// 収支差額、支出金額合計は存在するが、収入金額合計が存在しない場合はエラーとする。
			errorList.add("収支差額を使用する場合は収入金額合計を追加して下さい。");
		}

		return errorList;
	}

	/**
	 * 申請内容リストマップを配列に変換する<br>
	 * （ユーザー定義届書追加の複製モード時、ユーザー定義届書変更時に共通で使用）
	 * 
	 * @param listMap リストマップ
	 * @return 配列
	 */
	public String[] toArrayListMapData(List<GMap> listMap){
		List<String> listRtn = new ArrayList<String>();

		for(GMap map : listMap) {
			StringBuilder sb = new StringBuilder();
			sb.append(map.get("item_name") == null ? "" : map.get("item_name").toString()); // 項目名
			sb.append("\r\n");
			sb.append(map.get("area_kbn") == null ? "" : map.get("area_kbn").toString()); // エリア区分
			sb.append("\r\n");
			sb.append(map.get("hyouji_jun") == null ? "" : map.get("hyouji_jun").toString()); // 表示順
			sb.append("\r\n");
			sb.append(map.get("denpyou_edano") == null ? "" : map.get("denpyou_edano").toString()); // 伝票枝番号
			sb.append("\r\n");
			sb.append(map.get("hissu_flg") == null ? "" : map.get("hissu_flg").toString()); // 必須フラグ
			sb.append("\r\n");
			sb.append(map.get("max_length") == null ? "" : map.get("max_length").toString()); // 最大桁数
			sb.append("\r\n");
			sb.append(map.get("min_value") == null ? "" : map.get("min_value").toString()); // 最小値
			sb.append("\r\n");
			sb.append(map.get("max_value") == null ? "" : map.get("max_value").toString()); // 最大値
			sb.append("\r\n");
			sb.append(map.get("label_name") == null ? "" : map.get("label_name").toString()); // ラベル名
			sb.append("\r\n");
			sb.append(map.get("buhin_type") == null ? "" : map.get("buhin_type").toString()); // 部品タイプ
			sb.append("\r\n");
			sb.append(map.get("buhin_format") == null ? "" : map.get("buhin_format").toString()); // 部品形式
			sb.append("\r\n");
			sb.append(map.get("buhin_width") == null ? "" : map.get("buhin_width").toString()); // 部品幅
			sb.append("\r\n");
			sb.append(map.get("buhin_height") == null ? "" : map.get("buhin_height").toString()); // 部品高さ
			sb.append("\r\n");
			sb.append(map.get("checkbox_label_name") == null ? "" : map.get("checkbox_label_name").toString()); // チェックボックスラベル名
			sb.append("\r\n");
			sb.append(map.get("master_kbn") == null ? "" : map.get("master_kbn").toString()); // マスター区分
			sb.append("\r\n");
			sb.append(map.get("yosan_shikkou_koumoku_id") == null ? "" : map.get("yosan_shikkou_koumoku_id").toString()); // 予算執行項目ID
			sb.append("\r\n");
			sb.append(map.get("decimal_point") == null ? "" : map.get("decimal_point").toString()); // 小数点以下桁数
			sb.append("\r\n");
			sb.append(map.get("kotei_hyouji") == null ? "" : map.get("kotei_hyouji").toString()); // 固定表示
			
			listRtn.add(sb.toString());
		}

		return listRtn.toArray(new String[0]);
	}

	/**
	 * 明細リストマップを配列に変換する<br>
	 * （ユーザー定義届書追加の複製モード時、ユーザー定義届書変更時に共通で使用）
	 * 
	 * @param listMap リストマップ
	 * @return 配列
	 */
	public String[] toArrayListMapOption(List<GMap> listMap){
		List<String> listRtn = new ArrayList<String>();

		for(GMap map : listMap) {
			StringBuilder sb = new StringBuilder();
			sb.append(map.get("item_name") == null ? "" : map.get("item_name").toString()); // 項目名
			sb.append("\r\n");
			sb.append(map.get("area_kbn") == null ? "" : map.get("area_kbn").toString()); // エリア区分
			sb.append("\r\n");
			sb.append(map.get("hyouji_jun") == null ? "" : map.get("hyouji_jun").toString()); // 表示順
			sb.append("\r\n");
			sb.append(map.get("text") == null ? "" : map.get("text").toString()); // テキスト
			sb.append("\r\n");
			sb.append(map.get("value") == null ? "" : map.get("value").toString()); // 値
			sb.append("\r\n");
			sb.append(map.get("select_item") == null ? "" : map.get("select_item").toString()); // 選択項目名
			listRtn.add(sb.toString());
		}

		return listRtn.toArray(new String[0]);
	}
	
	/**
	 * 指定した予算執行項目のitem_nameを取得する
	 * @param denpyouKbn 伝票区分
	 * @param version ヴァージョン
	 * @param yosanShikkouItemID 予算執行項目ID
	 * @return item_name
	 */
	public String getYosanShikkouItemName(String denpyouKbn, String version, String yosanShikkouItemID){
		String sql = "SELECT item_name FROM kani_todoke_koumoku WHERE denpyou_kbn = ? AND version = ? AND yosan_shikkou_koumoku_id = ?";
		GMap ret = connection.find(sql, denpyouKbn, Integer.parseInt(version), yosanShikkouItemID);
		return (ret == null)? "" : ret.get("item_name").toString();
	}

    
    /**
     * select_itemの変更に伴い、届出をヴァージョンアップする。
     * @param user ユーザ
     * @param denpyouKbn 伝票区分
     * @param version ヴァージョン
     * @param selectItemList 選択項目
     */
    public void updateSelectItem(User user, String denpyouKbn, int version, List<String> selectItemList) {
    	KaniTodokeVersionDao kaniVersionDao  = EteamContainer.getComponent(KaniTodokeVersionDao.class , connection);
    	KaniTodokeKoumokuDao kaniKoumokuDao  = EteamContainer.getComponent(KaniTodokeKoumokuDao.class , connection);
    	KaniTodokeTextDao kaniTextDao  = EteamContainer.getComponent(KaniTodokeTextDao.class , connection);
    	KaniTodokeTextareaDao kaniTextareaDao  = EteamContainer.getComponent(KaniTodokeTextareaDao.class , connection);
    	KaniTodokeCheckboxDao kaniCheckboxDao  = EteamContainer.getComponent(KaniTodokeCheckboxDao.class , connection);
    	KaniTodokeListOyaDao kaniListOyaDao  = EteamContainer.getComponent(KaniTodokeListOyaDao.class , connection);
    	KaniTodokeListKoDao kaniListKoDao  = EteamContainer.getComponent(KaniTodokeListKoDao.class , connection);
    	KaniTodokeMasterDao kaniMasterDao  = EteamContainer.getComponent(KaniTodokeMasterDao.class , connection);
    	KaniTodokeSelectItemDao selectItemDao  = EteamContainer.getComponent(KaniTodokeSelectItemDao.class, connection);
    	DenpyouShubetsuIchiranDao denpyouShubetsuIchiranDao = EteamContainer.getComponent(DenpyouShubetsuIchiranDao.class, connection);

    	//--------------------
    	//とりあえずVersionでVUP
    	//--------------------
    	KaniTodokeVersion kaniVersion = kaniVersionDao.find(denpyouKbn, version);
    	kaniVersion.version++;
    	kaniVersionDao.insert(kaniVersion, user.getTourokuOrKoushinUserId());
    	
    	List<KaniTodokeKoumoku> kaniKoumokuList = kaniKoumokuDao.load(denpyouKbn, version);
    	for(KaniTodokeKoumoku kaniKoumoku : kaniKoumokuList) {
    		kaniKoumoku.version++;
    		kaniKoumokuDao.insert(kaniKoumoku, user.getTourokuOrKoushinUserId());
    		
    	}
    	
    	List<KaniTodokeText> kaniTextList = kaniTextDao.load(denpyouKbn, version);
    	for(KaniTodokeText kaniText : kaniTextList) {
    		kaniText.version++;
    		kaniTextDao.insert(kaniText, user.getTourokuOrKoushinUserId());
    		
    	}
    	
    	List<KaniTodokeTextarea> kaniTextareaList = kaniTextareaDao.load(denpyouKbn, version);
    	for(KaniTodokeTextarea kaniTextarea : kaniTextareaList) {
    		kaniTextarea.version++;
    		kaniTextareaDao.insert(kaniTextarea, user.getTourokuOrKoushinUserId());
    		
    	}
    	
    	List<KaniTodokeCheckbox> kaniCheckboxList = kaniCheckboxDao.load(denpyouKbn, version);
    	for(KaniTodokeCheckbox kaniCheckbox : kaniCheckboxList) {
    		kaniCheckbox.version++;
    		kaniCheckboxDao.insert(kaniCheckbox, user.getTourokuOrKoushinUserId());
    		
    	}
    	
    	List<KaniTodokeListOya> kaniListOyaList = kaniListOyaDao.load(denpyouKbn, version);
    	for(KaniTodokeListOya kaniListOya : kaniListOyaList) {
    		kaniListOya.version++;
    		kaniListOyaDao.insert(kaniListOya, user.getTourokuOrKoushinUserId());
    		
    	}
    	
    	List<KaniTodokeListKo> kaniListKoList = kaniListKoDao.load(denpyouKbn, version);
    	for(KaniTodokeListKo kaniListKo : kaniListKoList) {
    		kaniListKo.version++;
    		kaniListKoDao.insert(kaniListKo, user.getTourokuOrKoushinUserId());
    		
    	}
    	
    	List<KaniTodokeMaster> kaniMasterList = kaniMasterDao.load(denpyouKbn, version);
    	for(KaniTodokeMaster kaniMaster : kaniMasterList) {
    		kaniMaster.version++;
    		kaniMasterDao.insert(kaniMaster, user.getTourokuOrKoushinUserId());
    		
    	}
    	
    	// 伝票種別一覧も上げとかないと新規起票で新しいヴァージョンにならない
    	DenpyouShubetsuIchiran denpyouShubetsuIchiran = denpyouShubetsuIchiranDao.find(denpyouKbn);
    	denpyouShubetsuIchiran.version++;
    	denpyouShubetsuIchiranDao.update(denpyouShubetsuIchiran, user.getTourokuOrKoushinUserId());
    	
    	version++;

    	//--------------------
    	//変更した選択項目に関してのみ洗い替え
    	//--------------------
		//同じselect_itemが１伝票に複数紐付く可能性があるので紐付き先の項目単位に
    	for(String selectItem : selectItemList) {
	    	List<GMap> todokeList = kaniListKoDao.loadBySelectItem(denpyouKbn, version, selectItem);
			for(GMap todoke : todokeList) {
				//とりあえず該当項目に紐付くcd/valueの組を消す ※cd = blankは残す
				kaniListKoDao.deleteWithoutBlank(denpyouKbn, version, todoke.get("area_kbn"), todoke.get("item_name"));
	
				//マスター「kani_todoke_select_item」のcd/valueの組をkani_todoke_list_koにコピー
				List<KaniTodokeSelectItem> selectItemValueList = selectItemDao.load(selectItem);
				int hyoujiJun = 1;
				if(kaniListKoDao.exists(denpyouKbn, version, todoke.get("area_kbn"), todoke.get("item_name"), 1)) {
					//※cd = blankを残している場合
					hyoujiJun = 2;
				}
				for(KaniTodokeSelectItem selectItemValue : selectItemValueList) {
					KaniTodokeListKo ko = new KaniTodokeListKo();
					ko.denpyouKbn  = denpyouKbn;
					ko.version  = version;
					ko.areaKbn  = todoke.get("area_kbn");
					ko.itemName  = todoke.get("item_name");
					ko.hyoujiJun  = hyoujiJun++;
					ko.text  = selectItemValue.name;
					ko.value  = selectItemValue.cd;
					ko.selectItem  = selectItemValue.selectItem;
					kaniListKoDao.insert(ko, user.getTourokuOrKoushinUserId());
				}
	    	}
    	}
    }
}
