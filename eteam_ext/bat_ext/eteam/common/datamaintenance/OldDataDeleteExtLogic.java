package eteam.common.datamaintenance;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import eteam.common.EteamSettingInfo.Key;
import eteam.common.RegAccess;

/**
 * 過去データ削除ExtLogic
 */
public class OldDataDeleteExtLogic extends OldDataDeleteLogic {
	
	/** DELETE文 */
	protected static final String[][] SQLS = {
		{"イベントログ", "DELETE FROM event_log WHERE date(end_time) < ?", Key.DATA_HOZON_NISSUU_LOG},
		{"バッチログ", "DELETE FROM batch_log WHERE date(end_time) < ?", Key.DATA_HOZON_NISSUU_LOG},
		{"セキュリティログ", "DELETE FROM SECURITY_LOG WHERE date(EVENT_TIME) < ?", Key.DATA_HOZON_NISSUU_LOG},
		{"インフォメーション", "DELETE FROM information WHERE tsuuchi_kikan_to < current_date AND date(koushin_time) < ?", Key.DATA_HOZON_NISSUU},
		{"インフォメーション順序", "DELETE FROM information_sort a WHERE NOT EXISTS (SELECT * FROM information b WHERE a.info_id = b.info_id)", Key.DATA_HOZON_NISSUU},
		{"インフォメーションID採番", "DELETE FROM info_id_saiban WHERE touroku_date < ?", Key.DATA_HOZON_NISSUU},
		{"メール通知設定", "DELETE FROM mail_tsuuchi a WHERE NOT EXISTS (SELECT * FROM user_info b WHERE a.user_id = b.user_id)", Key.DATA_HOZON_NISSUU},
		{"ブックマーク", "DELETE FROM bookmark a WHERE NOT EXISTS (SELECT * FROM user_info b WHERE a.user_id = b.user_id)", Key.DATA_HOZON_NISSUU},
		{"伝票", "DELETE FROM denpyou WHERE date(koushin_time) < ? AND denpyou_joutai NOT IN('10','20') AND (denpyou_kbn LIKE 'A%' OR denpyou_kbn LIKE 'B%')", Key.DATA_HOZON_NISSUU},
		{"通知", "DELETE FROM tsuuchi  a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"承認ルート", "DELETE FROM shounin_route a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"承認ルート合議親", "DELETE FROM shounin_route_gougi_oya a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"承認ルート合議子", "DELETE FROM shounin_route_gougi_ko a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"承認状況", "DELETE FROM shounin_joukyou a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"任意メモ", "DELETE FROM nini_comment a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"添付ファイル", "DELETE FROM tenpu_file a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"e文書ファイル", "DELETE FROM ebunsho_file a WHERE NOT EXISTS (SELECT * FROM tenpu_file b WHERE (a.denpyou_id,a.edano) = (b.denpyou_id,b.edano)) AND (substr(a.denpyou_id, 8, 4) LIKE 'A%' OR substr(a.denpyou_id, 8, 4) LIKE 'B%')", Key.DATA_HOZON_NISSUU},
		{"e文書データ", "DELETE FROM ebunsho_data a :JOUKEN SQL", Key.DATA_HOZON_NISSUU},
		{"関連伝票", "DELETE FROM kanren_denpyou a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"伝票ID採番", "DELETE FROM denpyou_id_saiban WHERE touroku_date < ?", Key.DATA_HOZON_NISSUU},
		{"伝票一覧", "DELETE FROM denpyou_ichiran a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"請求書払い", "DELETE FROM seikyuushobarai a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"請求書払い明細", "DELETE FROM seikyuushobarai_meisai a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"旅費仮払", "DELETE FROM ryohi_karibarai a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"旅費仮払明細", "DELETE FROM ryohi_karibarai_meisai a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"旅費仮払経費明細", "DELETE FROM ryohi_karibarai_keihi_meisai a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"旅費精算", "DELETE FROM ryohiseisan a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"旅費精算明細", "DELETE FROM ryohiseisan_meisai a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"旅費精算経費明細", "DELETE FROM ryohiseisan_keihi_meisai a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"交通費精算", "DELETE FROM koutsuuhiseisan a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"交通費精算明細", "DELETE FROM koutsuuhiseisan_meisai a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"通勤定期", "DELETE FROM tsuukinteiki a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"仮払", "DELETE FROM karibarai a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"経費精算", "DELETE FROM keihiseisan a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"経費精算明細", "DELETE FROM keihiseisan_meisai a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"振替", "DELETE FROM furikae a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"付替", "DELETE FROM tsukekae a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"付替明細", "DELETE FROM tsukekae_meisai a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"自動引落", "DELETE FROM jidouhikiotoshi a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"自動引落明細", "DELETE FROM jidouhikiotoshi_meisai a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"海外旅費仮払", "DELETE FROM kaigai_ryohi_karibarai a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"海外旅費仮払経費明細", "DELETE FROM kaigai_ryohi_karibarai_keihi_meisai a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"海外旅費仮払明細", "DELETE FROM kaigai_ryohi_karibarai_meisai a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"海外旅費精算", "DELETE FROM kaigai_ryohiseisan a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"海外旅費精算経費明細", "DELETE FROM kaigai_ryohiseisan_keihi_meisai a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"海外旅費精算明細", "DELETE FROM kaigai_ryohiseisan_meisai a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"支払依頼", "DELETE FROM shiharai_irai a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"支払依頼Ext","DELETE FROM shiharai_irai_ext a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},//◀◀カスタマイズ追加
		{"支払依頼明細", "DELETE FROM shiharai_irai_meisai a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"仕訳抽出(de3)", "DELETE FROM shiwake_de3 a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"仕訳抽出(SIAS)", "DELETE FROM shiwake_sias a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"FB抽出", "DELETE FROM fb a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"仕訳パターンマスター", "DELETE FROM shiwake_pattern_master a WHERE   delete_flg = '1'   AND date(koushin_time) < ?   AND NOT (a.denpyou_kbn,a.shiwake_edano) IN(     (SELECT 'A001' as denpyou_kbn, COALESCE(shiwake_edano,-1) FROM keihiseisan_meisai)     UNION     (SELECT 'A001' as denpyou_kbn, COALESCE(shiwake_edano,-1) FROM ryohiseisan_keihi_meisai)     UNION     (SELECT 'A001' as denpyou_kbn, COALESCE(shiwake_edano,-1) FROM ryohi_karibarai_keihi_meisai)     UNION     (SELECT 'A001' as denpyou_kbn, COALESCE(shiwake_edano,-1) FROM kaigai_ryohiseisan_keihi_meisai WHERE kaigai_flg<>'1')     UNION     (SELECT 'A001' as denpyou_kbn, COALESCE(shiwake_edano,-1) FROM kaigai_ryohi_karibarai_keihi_meisai WHERE kaigai_flg<>'1')      UNION     (SELECT 'A901' as denpyou_kbn, COALESCE(shiwake_edano,-1) FROM kaigai_ryohiseisan_keihi_meisai WHERE kaigai_flg='1')     UNION     (SELECT 'A901' as denpyou_kbn, COALESCE(shiwake_edano,-1) FROM kaigai_ryohi_karibarai_keihi_meisai WHERE kaigai_flg='1')      UNION     (SELECT 'A002' as denpyou_kbn, COALESCE(shiwake_edano,-1) FROM karibarai)     UNION     (SELECT 'A003' as denpyou_kbn, COALESCE(shiwake_edano,-1) FROM seikyuushobarai_meisai)     UNION     (SELECT 'A004' as denpyou_kbn, COALESCE(shiwake_edano,-1) FROM ryohiseisan)     UNION     (SELECT 'A005' as denpyou_kbn, COALESCE(shiwake_edano,-1) FROM ryohi_karibarai)     UNION     (SELECT 'A006' as denpyou_kbn, COALESCE(shiwake_edano,-1) FROM tsuukinteiki)     UNION     (SELECT 'A009' as denpyou_kbn, COALESCE(shiwake_edano,-1) FROM jidouhikiotoshi_meisai)     UNION     (SELECT 'A010' as denpyou_kbn, COALESCE(shiwake_edano,-1) FROM koutsuuhiseisan)     UNION     (SELECT 'A011' as denpyou_kbn, COALESCE(shiwake_edano,-1) FROM kaigai_ryohiseisan)     UNION     (SELECT 'A012' as denpyou_kbn, COALESCE(shiwake_edano,-1) FROM kaigai_ryohi_karibarai))", Key.DATA_HOZON_NISSUU},
		{"（期別）財務仕訳", "DELETE FROM ki_shiwake WHERE dymd < ?", Key.DATA_HOZON_NISSUU},
		{"マスター管理版数", "DELETE FROM master_kanri_hansuu a WHERE a.version != (SELECT MAX(version) FROM master_kanri_hansuu b WHERE a.master_id = b.master_id) AND date(koushin_time) < ?", Key.DATA_HOZON_NISSUU_LOG},
		{"一覧表示項目制御", "DELETE FROM LIST_ITEM_CONTROL a WHERE NOT EXISTS (SELECT * FROM user_info b WHERE a.user_id = b.user_id)", Key.DATA_HOZON_NISSUU},
		{"定期券情報", "DELETE FROM teiki_jouhou WHERE shiyou_shuuryoubi < current_date AND date(koushin_time) < ?", Key.DATA_HOZON_NISSUU},
		{"届出ジェネレータ", "DELETE FROM kani_todoke a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"届出ジェネレータ明細", "DELETE FROM kani_todoke_meisai a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"届出ジェネレータ項目定義", "DELETE FROM kani_todoke_koumoku a WHERE NOT EXISTS (SELECT * FROM kani_todoke_version b WHERE a.denpyou_kbn = b.denpyou_kbn AND a.version = b.version)", Key.DATA_HOZON_NISSUU},
		{"届出ジェネレータバージョン", "DELETE FROM kani_todoke_version a  WHERE a.version !=          (SELECT max(version)          FROM kani_todoke_version b          WHERE a.denpyou_kbn = b.denpyou_kbn)    AND NOT EXISTS          (SELECT * FROM kani_todoke c          WHERE a.denpyou_kbn = c.denpyou_kbn          AND a.version = c.version)    AND date(koushin_time) < ?", Key.DATA_HOZON_NISSUU},
		{"届出ジェネレータ項目テキストエリア", "DELETE FROM kani_todoke_textarea a WHERE NOT EXISTS (SELECT * FROM kani_todoke_version b WHERE a.denpyou_kbn = b.denpyou_kbn AND a.version = b.version)", Key.DATA_HOZON_NISSUU},
		{"届出ジェネレータ項目テキスト", "DELETE FROM kani_todoke_text a WHERE NOT EXISTS (SELECT * FROM kani_todoke_version b WHERE a.denpyou_kbn = b.denpyou_kbn AND a.version = b.version)", Key.DATA_HOZON_NISSUU},
		{"届出ジェネレータ項目チェックボックス", "DELETE FROM kani_todoke_checkbox a WHERE NOT EXISTS (SELECT * FROM kani_todoke_version b WHERE a.denpyou_kbn = b.denpyou_kbn AND a.version = b.version)", Key.DATA_HOZON_NISSUU},
		{"届出ジェネレータ項目リスト親", "DELETE FROM kani_todoke_list_oya a WHERE NOT EXISTS (SELECT * FROM kani_todoke_version b WHERE a.denpyou_kbn = b.denpyou_kbn AND a.version = b.version)", Key.DATA_HOZON_NISSUU},
		{"届出ジェネレータ項目リスト子", "DELETE FROM kani_todoke_list_ko a WHERE NOT EXISTS (SELECT * FROM kani_todoke_version b WHERE a.denpyou_kbn = b.denpyou_kbn AND a.version = b.version)", Key.DATA_HOZON_NISSUU},
		{"届出ジェネレータ項目マスター", "DELETE FROM kani_todoke_master a WHERE NOT EXISTS (SELECT * FROM kani_todoke_version b WHERE a.denpyou_kbn = b.denpyou_kbn AND a.version = b.version)", Key.DATA_HOZON_NISSUU},
		{"届出ジェネレータサマリ", "DELETE FROM kani_todoke_summary a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"届出ジェネレータ一覧", "DELETE FROM kani_todoke_ichiran a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"届出ジェネレータ明細一覧", "DELETE FROM kani_todoke_meisai_ichiran a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"ICカード", "DELETE FROM ic_card a WHERE NOT EXISTS (SELECT * FROM user_info b WHERE a.user_id = b.user_id)", Key.DATA_HOZON_NISSUU},
		{"ICカード利用履歴", "DELETE FROM ic_card_rireki a WHERE NOT EXISTS (SELECT * FROM ic_card b WHERE a.ic_card_no = b.ic_card_no)", Key.DATA_HOZON_NISSUU},
		{"法人カードインポート", "DELETE FROM houjin_card_import a WHERE NOT EXISTS (SELECT * FROM user_info b WHERE a.user_id = b.user_id)", Key.DATA_HOZON_NISSUU},
		{"法人カード使用履歴情報", "DELETE FROM houjin_card_jouhou WHERE torikomi_denpyou_id = '' AND koushin_time < ?", Key.DATA_HOZON_NISSUU},
		{"執行状況一覧情報", "DELETE FROM shikkou_joukyou_ichiran_jouhou a WHERE NOT EXISTS (SELECT * FROM user_info b WHERE a.user_id = b.user_id)", Key.DATA_HOZON_NISSUU},
		{"予算・起案金額チェック", "DELETE FROM yosan_kiankingaku_check a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"予算・起案金額チェックコメント", "DELETE FROM yosan_kiankingaku_check_comment a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"伝票起案紐付", "DELETE FROM denpyou_kian_himozuke a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
		{"ユーザー別デフォルト値", "DELETE FROM user_default_value a WHERE NOT EXISTS (SELECT * FROM user_info b WHERE a.user_id = b.user_id)", Key.DATA_HOZON_NISSUU},
		{"添付伝票除外", "DELETE FROM tenpu_denpyou_jyogai a WHERE NOT EXISTS (SELECT * FROM denpyou b WHERE a.denpyou_id = b.denpyou_id)", Key.DATA_HOZON_NISSUU},
	};


	/**
	 * 過去データ削除SQL(WF一般)
	 * @param kijunDate 削除基準日
	 * @param category 削除対象カテゴリ
	 * @return 削除件数
	 */
	public List<Map<String,String>> updateOldDelete(Date kijunDate, String category) {
		List<Map<String, String>> deleteResultList = new ArrayList<>();
		for (String[] arr : SQLS) {
			String tableNameWa = arr[0]; 
			String sql = arr[1];
			//e文書データのみ、「経費のみ」「拠点のみ」「経費拠点両方」使用しているかどうかでSQLの削除条件を置換する。
			if(tableNameWa.equals("e文書データ")) {
				if(!RegAccess.checkEnableZaimuKyotenOption()) {
					sql = sql.replace(":JOUKEN SQL", "WHERE NOT EXISTS (SELECT * FROM ebunsho_file b WHERE a.ebunsho_no = b.ebunsho_no)");
				}else {
					sql = sql.replace(":JOUKEN SQL", "WHERE "
							+ "  NOT EXISTS ( "
							+ "    ( "
							+ "      SELECT "
							+ "        b.ebunsho_no "
							+ "      FROM "
							+ "        ebunsho_file b "
							+ "      WHERE "
							+ "        a.ebunsho_no = b.ebunsho_no "
							+ "    ) "
							+ "    UNION ( "
							+ "      SELECT "
							+ "        c.ebunsho_no "
							+ "      FROM "
							+ "        user_tenpu_file c "
							+ "        INNER JOIN meisai_tenpu_file_himoduke d "
							+ "          ON c.tenpu_file_serial_no = d.tenpu_file_serial_no "
							+ "      WHERE "
							+ "        a.ebunsho_no = c.ebunsho_no "
							+ "    ) "
							+ "  ) ");
				}
			}
			if(arr[2].equals(category)){
				Object[] params = new Object[StringUtils.countMatches(sql, "?")];
				for (int i = 0; i < params.length; i++) params[i] = kijunDate;
				
				int kekka  = connection.update(sql, params);

				Map<String, String> deleteMap = new HashMap<>();
				deleteMap.put("master_id", tableNameWa); //★mapのidに反して和名
				deleteMap.put("count", String.valueOf(kekka));
				deleteResultList.add(deleteMap);
			}
		}
		return deleteResultList;
	}
	
	
}